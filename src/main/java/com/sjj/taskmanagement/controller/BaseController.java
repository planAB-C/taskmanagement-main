package com.sjj.taskmanagement.controller;

import com.sjj.taskmanagement.service.*;
import com.sjj.taskmanagement.utils.MinioUtils;
import com.sjj.taskmanagement.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Autowired
    HttpServletRequest req;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    SysUserService sysUserService;

    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    SysUserRoleService sysUserRoleService;

    @Autowired
    SysRoleMenuService sysRoleMenuService;

    @Autowired
    SysFileService sysFileService;

    @Autowired
    SysTaskService sysTaskService;

    @Autowired
    RecyclerService recyclerService;

    @Autowired
    MinioUtils minioUtils;

    @Autowired
    SysRegisterService sysRegisterService;
}
