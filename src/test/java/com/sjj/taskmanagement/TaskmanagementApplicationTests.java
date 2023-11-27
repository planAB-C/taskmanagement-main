package com.sjj.taskmanagement;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sjj.taskmanagement.common.entities.SysMenu;
import com.sjj.taskmanagement.common.entities.SysRole;
import com.sjj.taskmanagement.common.entities.SysUser;
import com.sjj.taskmanagement.controller.BaseController;
import com.sjj.taskmanagement.controller.ScheduleTask;
import com.sjj.taskmanagement.mapper.SysUserMapper;
import com.sjj.taskmanagement.service.SysMenuService;
import com.sjj.taskmanagement.service.SysRegisterService;
import com.sjj.taskmanagement.service.SysRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class TaskmanagementApplicationTests extends BaseController {
    @Autowired
    SysRegisterService sysRegisterService;
    @Resource
    SysUserMapper sysUserMapper;
    @Autowired
    SysRoleService sysRoleService;

    @Autowired
    SysMenuService sysMenuService;

    @Autowired
    ScheduleTask scheduleTask;
    @Test
    public void test() {
        scheduleTask.settlement();
    }

}
