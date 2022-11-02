package com.sjj.taskmanagement.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.taskmanagement.common.entities.Constraint;
import com.sjj.taskmanagement.common.entities.ResultBody;
import com.sjj.taskmanagement.common.entities.SysUser;
import com.sjj.taskmanagement.mapper.SysUserMapper;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sjj
 * @since 2021-10-17
 */

@Api(tags = "系统用户接口")
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends BaseController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    SysUserMapper sysUserMapper;
    /**
     * @Author sjj
     * @Description //TODO 注册用户的接口
     * @Date 2021/10/17 2021/10/17
     * @Param [sysUser]
     * @return com.sjj.taskmanagement.common.entities.ResultBody
     */
    @ApiOperation(value = "注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "用户类", required = true, dataType = "SysUser"),
    })
    @PostMapping("/register")
    @Transactional//开启事务
    public ResultBody register(@Validated @RequestBody SysUser sysUser)
    {
        sysUser = sysUserService.register(sysUser);
        return ResultBody.success(sysUser);
    }
    @Transactional//开启事务
    @ApiOperation(value = "上传头像")
    @PostMapping("/upload")
    public ResultBody upload(@RequestParam("uploadFile") MultipartFile zipFile) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResultBody.success(sysUserService.uploadAvatar(zipFile));
    }
    @GetMapping("/current")
    @ApiOperation(value = "获取当前用户详情")
    public ResultBody current()
    {
        return ResultBody.success(sysUserService.getCurrentUser());
    }

    @GetMapping("/list")
    @ApiOperation(value = "获取用户列表")
    public ResultBody usernameList()
    {
        return ResultBody.success(sysUserService.getUsernameList());
    }

    @PostMapping("/logout")//注销用户
    @Transactional//开启事务
    public ResultBody logout(@Validated @RequestBody String Username)
    {
        SysUser sysUser=sysUserService.getByUsername(Username);
        sysUserService.logoutById(sysUser.getId());
        return ResultBody.success(sysUser.getUsername()+"注销成功！");
    }

    @PostMapping("/updatePassword")//注销用户
    @Transactional//开启事务
    public ResultBody updatePassword(@Validated @RequestBody SysUser sysUser)
    {
        sysUserService.updatePassword(sysUser);
        return ResultBody.success("密码修改成功");
    }


}
