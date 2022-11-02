package com.sjj.taskmanagement.controller;

import cn.hutool.core.util.BooleanUtil;
import com.sjj.taskmanagement.common.entities.Constraint;
import com.sjj.taskmanagement.common.entities.ResultBody;
import com.sjj.taskmanagement.service.SysRegisterService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Api(tags = "位置接口")
@RestController
@Slf4j
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate ;

    @Autowired
    private SysRegisterService sysRegisterService ;

    private String gaoDekey= Constraint.GAODE_KEY;//高德地图应用的key值

    @GetMapping("/map")
    public Map<?, ?> map( String location ) {
        //接受经纬度并且转化成高德地理编码
        Map<?, ?> gaoDeCode = restTemplate.getForObject("https://restapi.amap.com/v3/assistant/coordinate/convert?locations="
                +location+"&coordsys=gps&output=JSON&key="+gaoDekey,Map.class);
        System.out.println(gaoDekey);
        //将高德地理编码逆地理编码成所在地名称并返回给前端
        Map<?, ?> gaoDeLocation = restTemplate.getForObject("https://restapi.amap.com/v3/geocode/regeo?output=JSON&location="
                +gaoDeCode.get("locations")+"&radius=10&extensions=all&key="+gaoDekey,Map.class);
        log.info("---获取到定位---"+gaoDeLocation);
        return gaoDeLocation;
    }

    /**
     * 开关，如果以前是开，传入1，传出0
     * @param state 开关的状态
     * @return
     */
    @GetMapping("/switch")
    public ResultBody turn(Integer state){
        stringRedisTemplate.opsForValue().set("location:state:",state.toString());
        return ResultBody.success(state) ;
    }

    @GetMapping("/switchState")
    public ResultBody switchState(){
        String state = stringRedisTemplate.opsForValue().get("location:state:");
        return ResultBody.success(state);
    }

    @GetMapping("/signOn")
    public ResultBody locationSignOn(String username , double x ,double y){
        //查看是否打开开关
        String s = stringRedisTemplate.opsForValue().get("location:state:");
        //未打开开关，返回使用正常签到
        int state = Integer.parseInt(s);
        if (state==0){
            return ResultBody.success(sysRegisterService.addRegister(username)) ;
        }
        //打开开关，开始位置签到
        //查看是否调用成功
        boolean b = sysRegisterService.locationSignIn(username, x, y);
        //不成功，直接返回fail
        if (!BooleanUtil.isTrue(b)){
            return ResultBody.error("打卡失败") ;
        }
        //成功，进行跳转
        return ResultBody.success();
    }
}
