package com.sjj.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.taskmanagement.common.entities.*;
import com.sjj.taskmanagement.mapper.SysRegisterMapper;
import com.sjj.taskmanagement.mapper.SysUserMapper;
import com.sjj.taskmanagement.service.SysMenuService;
import com.sjj.taskmanagement.service.SysRoleService;
import com.sjj.taskmanagement.service.SysUserRoleService;
import com.sjj.taskmanagement.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjj.taskmanagement.utils.MinioUtils;
import com.sjj.taskmanagement.utils.RedisUtil;
import io.minio.Result;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sjj
 * @since 2021-10-17
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    SysRoleService sysRoleService;

   // @Autowired
  //  SysRegisterMapper sysRegisterMapper;

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    SysUserRoleService sysUserRoleService;
    @Autowired
    private MinioUtils minioUtils;
    @Override
    public SysUser getByUsername(String username) {
        return getOne(new QueryWrapper<SysUser>().eq("username", username));
    }

    @Override
    public void logoutById(Long id){
        sysUserMapper.deleteById(id);
      //  sysRegisterMapper.deleteById(id);
    }

    @Override
    public List<String> getUsernameList(){

       return sysUserMapper.getUsernameList();
    }

    /*
 * @Author sjj
 * @Description //TODO 根据用户ID获取权限字符串
 * @Date 2021/10/17 2021/10/17
 * @Param userId :用户id
 * @return 返回权限字符串
 **/

    @Override
    public String getUserAuthorityInfo(Long userId) {

        SysUser sysUser = sysUserMapper.selectById(userId);

        //  ROLE_admin,ROLE_normal,sys:user:list,....
        String authority = "";

        if (redisUtil.hasKey("GrantedAuthority:" + sysUser.getUsername())) {
            authority = (String) redisUtil.get("GrantedAuthority:" + sysUser.getUsername());

        } else {
            // 获取角色编码
            List<SysRole> roles = sysRoleService.list(new QueryWrapper<SysRole>()
                    .inSql("id", "select role_id from sys_user_role where user_id = " + userId));

            if (roles.size() > 0) {
                String roleCodes = roles.stream().map(r -> "ROLE_" + r.getCode()).collect(Collectors.joining(","));
                authority = roleCodes.concat(",");
            }

            // 获取菜单操作编码
            List<Long> menuIds = sysUserMapper.getNavMenuIds(userId);
            if (menuIds.size() > 0) {

                List<SysMenu> menus = sysMenuService.listByIds(menuIds);
                String menuPerms = menus.stream().map(m -> m.getPerms()).collect(Collectors.joining(","));

                authority = authority.concat(menuPerms);
            }

            redisUtil.set("GrantedAuthority:" + sysUser.getUsername(), authority, 60 * 60);
        }
        return authority;
    }
    /*
     * @Author sjj
     * @Description //TODO 以下方法都为删除用户权限缓存的方法
     * @Date 2021/10/17 2021/10/17
     **/

    @Override
    public void clearUserAuthorityInfo(String username) {
        redisUtil.del("GrantedAuthority:" + username);
    }

    @Override
    public void clearUserAuthorityInfoByRoleId(Long roleId) {

        List<SysUser> sysUsers = this.list(new QueryWrapper<SysUser>()
                .inSql("id", "select user_id from sys_user_role where role_id = " + roleId));

        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });

    }

    @Override
    public void clearUserAuthorityInfoByMenuId(Long menuId) {
        List<SysUser> sysUsers = sysUserMapper.listByMenuId(menuId);

        sysUsers.forEach(u -> {
            this.clearUserAuthorityInfo(u.getUsername());
        });
    }

    @Override
    public SysUser register(SysUser sysUser) {
        //设置密码
        String password=passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(password);
        //设置头像
        sysUser.setAvatar(Constraint.DEFULT_AVATAR);
        sysUser.setStatu(Constraint.STATUS_ON);
        //插入数据
        this.save(sysUser);
        //分配默认角色为普通用户
        SysUserRole sysUserRole=new SysUserRole();
        sysUserRole.setUserId(sysUser.getId())
                .setRoleId(3L);
        sysUserRoleService.save(sysUserRole);
        return sysUser;
    }

    @Override
    public void updatePassword(SysUser sysUser) {
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUserMapper.update(sysUser,Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername,sysUser.getUsername()));
    }

    @Override
    public boolean uploadAvatar(MultipartFile zipFile) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //获取当前用户名
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String fileSuffix = getFileSuffix(zipFile);
        if (fileSuffix != null) {   // 拼接后缀
            username += fileSuffix;
        }
        //将文件存入minio之中
        minioUtils.putObject(minioUtils.getBucketName(),zipFile,"avatar/"+username,"multipart/form-data") ;
        log.info("{}上传成功！",username);
        //将文件url存到数据库
        String url="http://47.106.183.36:9000/taskmanagement/avatar/"+username;
        SysUser sysUser=new SysUser();
        sysUser.setAvatar(url);
        QueryWrapper<SysUser> wrapper=new QueryWrapper<>();
        wrapper.eq("username",(String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        update(sysUser,wrapper);
        return true;
    }

    @Override
    public SysUser getCurrentUser() {
        //获取当前用户名
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取当前用户
        SysUser sysUser = getByUsername(username);
        return sysUser;
    }

    //获取文件后缀
    public String getFileSuffix(MultipartFile file) {
        if (file == null) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex == -1) {    // 无后缀
            return null;
        } else {                    // 存在后缀
            return fileName.substring(suffixIndex, fileName.length());
        }
    }




}
