package com.sjj.taskmanagement.service;

import com.sjj.taskmanagement.common.entities.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sjj
 * @since 2021-10-17
 */
public interface SysUserService extends IService<SysUser> {
    /**
     * @Author sjj
     * @Description //TODO 由用户名获取用户的详细信息
     * @Date 2021/10/17 2021/10/17
     * @Param [username :用户名]
     * @return com.sjj.taskmanagement.common.entities.SysUser
     */

    SysUser getByUsername(String username);

    /**
     * @Author sjj
     * @Description //TODO 由用户id获得权限字符串
     * @Date 2021/10/17 2021/10/17
     * @Param [userId 用户id]
     * @return java.lang.String
     */

    String getUserAuthorityInfo(Long userId);
    /**
     * @Author sjj
     * @Description //TODO 清除权限的缓存
     * @Date 2021/10/17 2021/10/17
     * @Param [username 用户名]
     * @return void
     */


    void clearUserAuthorityInfo(String username);

    void clearUserAuthorityInfoByRoleId(Long roleId);

    void clearUserAuthorityInfoByMenuId(Long menuId);
    /**
     * @Author sjj
     * @Description //TODO 注册服务
     * @Date 2021/10/20 2021/10/20
     * @Param [sysUser]
     * @return com.sjj.taskmanagement.common.entities.SysUser
     */


    SysUser register(SysUser sysUser);
    /**
     *
     * @description 上传头像
     * <br/>
     * @param zipFile
     * @return boolean
     * @author sjj
     * @date 2022/1/6 18:54
     */
    boolean uploadAvatar(MultipartFile zipFile) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    SysUser getCurrentUser();

    void logoutById(Long id);
    List<String> getUsernameList();

    void updatePassword(SysUser sysUser);
}
