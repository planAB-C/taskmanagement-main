package com.sjj.taskmanagement.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sjj.taskmanagement.common.entities.SysRegister;
import com.sjj.taskmanagement.common.entities.SysReport;
import com.sjj.taskmanagement.common.entities.SysUser;
import com.sjj.taskmanagement.common.errorHandler.BizException;
import com.sjj.taskmanagement.mapper.SysRegisterMapper;
import com.sjj.taskmanagement.mapper.SysUserMapper;
import com.sjj.taskmanagement.service.MailService;
import com.sjj.taskmanagement.service.SysRegisterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjj.taskmanagement.service.SysReportService;
import com.sjj.taskmanagement.service.SysUserService;
import io.swagger.models.auth.In;
import org.aspectj.weaver.AjAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisCommands;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Array;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sjj
 * @since 2021-11-28
 */
@Service
@Transactional
public class SysRegisterServiceImpl extends ServiceImpl<SysRegisterMapper, SysRegister> implements SysRegisterService {
    @Autowired
    private MailService mailService;
    @Autowired
    private SysReportService sysReportService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRegisterMapper sysRegisterMapper ;
    @Autowired
    private SysUserMapper sysUserMapper ;
    @Autowired
    private StringRedisTemplate stringRedisTemplate ;
    @Autowired
    private SysRegisterService sysRegisterService ;
    @Override
    public boolean addRegister(String username) {
        QueryWrapper<SysRegister> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        SysRegister sysRegister=getOne(wrapper);
        if(sysRegister!=null)
        {
            if(sysRegister.getStatu().equals(1))
            {
                throw new BizException("该用户已经在签到，请签退之后重试！");
            }
            sysRegister.setStart(LocalDateTime.now(ZoneOffset.of("+8")));
            sysRegister.setUpdated(null);
            sysRegister.setStatu(1);
            return updateById(sysRegister);
        }

        else
        {
            sysRegister=new SysRegister();
            sysRegister.setUsername(username);
            sysRegister.setStart(LocalDateTime.now(ZoneOffset.of("+8")));
            sysRegister.setStatu(1);
            sysRegister.setTime(0D);
            return save(sysRegister);
        }
    }

    @Override
    public boolean finishRegister(String username) {
        QueryWrapper<SysRegister> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        SysRegister sysRegister=getOne(wrapper);
        if(sysRegister==null)
        {
            throw new BizException("签退用户不存在");
        }
        else if(sysRegister.getStatu().equals(0))
        {
            throw new BizException("该用户已经签退，请开始签到之后重试！");
        }
        else
        {
            sysRegister.setStatu(0);
            sysRegister.setUpdated(null);
            sysRegister.setTime(getRegisterTime(sysRegister.getStart(),LocalDateTime.now(ZoneOffset.of("+8")),sysRegister.getTime()));
            return updateById(sysRegister);
        }
    }

    @Override
    public List<SysRegister> getRegister(String username) {
        if(username==null)
        {
            QueryWrapper<SysRegister> wrapper=new QueryWrapper<>();
            wrapper.orderByDesc("time");
            return list(wrapper);
        }
        else
        {
            QueryWrapper<SysRegister> wrapper=new QueryWrapper<>();
            wrapper.eq("username",username);
            List<SysRegister> sysRegisterList=new ArrayList<>();
            sysRegisterList.add(getOne(wrapper));
            return sysRegisterList;
        }
    }

    @Override
    public boolean finishAllRegister() {
        QueryWrapper<SysRegister> wrapper=new QueryWrapper<>();
        wrapper.eq("statu",1);
        List<SysRegister>sysRegisterList=list(wrapper);
        for (SysRegister sysRegister : sysRegisterList) {
            finishWithNoTime(sysRegister.getUsername());
        }
        return true;
    }

    @Override
    public boolean clearAllRegister() {
        List<SysRegister> sysRegisterList=list();
        for (SysRegister sysRegister : sysRegisterList) {
            sysRegister.setTime(0D);
        }
        return updateBatchById(sysRegisterList);
    }

    @Override
    public boolean sendResult(String[] receivers) {
        QueryWrapper<SysRegister>wrapper=new QueryWrapper<>();
        wrapper.lt("time",20.0);

        List<SysRegister>sysRegisterList=list(wrapper);
        String content="尊敬的管理员：\n本周未完成签到的名单如下：\n";
        for (SysRegister sysRegister : sysRegisterList) {
            String res=sysRegister.getUsername()+":签到时间："+sysRegister.getTime()+"\n";
            content+=res;
        }
        for (String receiver : receivers) {
            mailService.sendSimpleMail(receiver,"本周签到未完成名单",content);
        }

        return true;
    }

    @Override
    public boolean clearOne(String username) {
        finishWithNoTime(username);
        return true;
    }

    @Override
    public List<SysRegister> listSpecifiedDirection(Integer id) {
        List<SysUser> userlist = sysUserMapper.selectList
                (new QueryWrapper<SysUser>()
                        .eq("direction_id", id));
        ArrayList<SysRegister> sysRegisters = new ArrayList<>();
        for (SysUser sysUser : userlist) {
            SysRegister user = sysRegisterMapper
                    .selectOne(new QueryWrapper<SysRegister>()
                            .eq("username", sysUser.getUsername()));
            if (user!=null){
                sysRegisters.add(user) ;
            }
        }

        return sysRegisters;
    }

    @Override
    public boolean locationSignIn(String username , double x ,double y) {
        //通过id用户信息
        SysRegister user = sysRegisterMapper.selectOne(new QueryWrapper<SysRegister>().eq("username", username));
        //获得用户的位置信息
        GeoOperations<String, String> opsForGeo = stringRedisTemplate.opsForGeo();
        ArrayList<RedisGeoCommands.GeoLocation> geo = new ArrayList<>();
        geo.add(new RedisGeoCommands.GeoLocation<>(username.toString(),new Point(x,y)));
        for (RedisGeoCommands.GeoLocation geoLocation : geo) {
            opsForGeo.add("202:position",geoLocation) ;
        }
        opsForGeo.add("202:position", new RedisGeoCommands.GeoLocation<>("position_202",new Point(110.414557,25.315651))) ;
        stringRedisTemplate.expire("202:position",10, TimeUnit.SECONDS) ;
        //将用户的位置信息与指定位置进行对比，如果不处于指定范围，报错
        Distance distance = stringRedisTemplate.opsForGeo().distance("202:position", "position_202", username.toString());
        if (distance.getValue()>(Double.valueOf(300)) ){
            return false ;
        }
        //处于指定范围，不报错，进行打卡
        //调用方法进行打卡
        boolean b = sysRegisterService.addRegister(user.getUsername());
        if (!BooleanUtil.isTrue(b)){
            return false ;
        }
        //返回true
        return true;
    }

    @Override
    public List<SysRegister> getRegisteringList() {
        QueryWrapper<SysRegister> wrapper=new QueryWrapper<>();
        wrapper.eq("statu",1);
        return list(wrapper);
    }

    @Override
    public boolean report(String username) {
        //获取当前用户名
        String reporter = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper<SysRegister> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        SysRegister sysRegister=getOne(wrapper);
        if(sysRegister==null)
        {
            throw new BizException("签退用户不存在");
        }
        else if(sysRegister.getStatu().equals(0))
        {
            throw new BizException("该用户已经签退，请开始签到之后重试！");
        }
        else
        {
            LocalDateTime end=LocalDateTime.now(ZoneOffset.of("+8"));
            LocalDateTime start=sysRegister.getStart();
            Long endTime= end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            Long startTime=start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            Double delayTimeHour=(((endTime-startTime)/1000.0)/60.0)/60.0;//计算本次签到的持续时长
            sysRegister.setStatu(0);
            sysRegister.setUpdated(null);
            //sysRegister.setTime(getRegisterTime(sysRegister.getStart(),LocalDateTime.now(ZoneOffset.of("+8")),sysRegister.getTime()));
            updateById(sysRegister);
            SysReport sysReport=new SysReport();
            sysReport.setReporter(reporter)
                    .setUsername(username)
                    .setTime(delayTimeHour)
                    .setStatu(0);
            sysReportService.save(sysReport);
        }

        return true;
    }

    @Override
    public boolean dead() {
        QueryWrapper<SysRegister>wrapper=new QueryWrapper<>();
        wrapper.lt("time",20);

        List<SysRegister>sysRegisterList=list(wrapper);
        List<SysUser> sysUserList=new ArrayList<>();
        for (SysRegister sysRegister : sysRegisterList) {
            QueryWrapper<SysUser> wrapper1=new QueryWrapper<>();
            wrapper1.eq("username",sysRegister.getUsername());
            SysUser sysUser=sysUserService.getOne(wrapper1);
            System.out.println(sysRegister);
            System.out.println(sysUser);
            sysUser.setDeadcount(sysUser.getDeadcount()-1);
            sysUserList.add(sysUser);
        }
        System.out.println(sysUserList);
        sysUserService.updateBatchById(sysUserList);
        return true;
    }

    private Double getRegisterTime(LocalDateTime start, LocalDateTime end,Double oldTime)
    {
        Long endTime= end.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Long startTime=start.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        Double delayTimeHour=(((endTime-startTime)/1000.0)/60.0)/60.0;//计算本次签到的持续时长
        return oldTime+delayTimeHour;
    }
    private void finishWithNoTime(String username)
    {
        QueryWrapper<SysRegister> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        SysRegister sysRegister=getOne(wrapper);
        if(sysRegister==null)
        {
            throw new BizException("签退用户不存在");
        }
        else if(sysRegister.getStatu().equals(0))
        {
            throw new BizException("该用户已经签退，请开始签到之后重试！");
        }
        else
        {
            sysRegister.setStatu(0);
            sysRegister.setUpdated(null);
            //sysRegister.setTime(getRegisterTime(sysRegister.getStart(),LocalDateTime.now(ZoneOffset.of("+8")),sysRegister.getTime()));
            updateById(sysRegister);
        }
    }
}
