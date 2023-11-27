package com.sjj.taskmanagement.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sjj.taskmanagement.common.entities.SysRegister;
import com.sjj.taskmanagement.common.entities.SysUser;
import com.sjj.taskmanagement.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author 梁贤卓
 * 定时任务
 */
@Component
public class ScheduleTask extends BaseController {

    @Autowired
    SysUserMapper sysUserMapper;
    @Scheduled(cron="0 59 23 ? * 7")
    public void settlement(){
        //强制所有人签退
        sysRegisterService.finishAllRegister();
        //扣除时长不达标人员的分
        List<SysUser> timeFalse=sysUserMapper.getTimeFalse(20.0);
        for(SysUser item : timeFalse){
            item.setDeadcount(item.getDeadcount()-3);
            sysUserMapper.update(item,Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername,item.getUsername()));
        }
        //邮箱发送不达标人员名单
        try {
//            String[] receivers = sysUserMapper.getAdminUserEmail().toArray(new String[0]);
            String[] receivers = {"2069762101@qq.com","1049175775@qq.com"};
            sysRegisterService.sendResult(receivers);
        }catch (Exception e){
        }finally {
            //清空签到时长
            sysRegisterService.clearAllRegister();
        }
    }

//    @Scheduled(cron="50 59 23 * * ?")
//    private void finishAllRegister(){
//        //强制所有人签退
//        sysRegisterService.finishAllRegister();
//    }

    @Scheduled(cron="20 10 8 ? * 3")
    private void Test(){

    }
}
