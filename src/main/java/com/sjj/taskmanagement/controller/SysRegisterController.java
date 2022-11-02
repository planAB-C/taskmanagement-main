package com.sjj.taskmanagement.controller;


import com.sjj.taskmanagement.common.entities.ResultBody;
import com.sjj.taskmanagement.common.entities.SysRegister;
import com.sjj.taskmanagement.service.SysRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sjj
 * @since 2021-11-28
 */
@RestController
@RequestMapping("/sys/register")
public class SysRegisterController extends BaseController {
    @Autowired
    private SysRegisterService sysRegisterService;
    /**
     * @Author sjj
     * @Description //TODO 签到接口
     * @Date 2021/11/28 2021/11/28
     * @Param [username]
     * @return com.sjj.taskmanagement.common.entities.ResultBody
     */

    @PostMapping("/register")
    public ResultBody register(@RequestParam("username")String username)
    {
        return ResultBody.success(sysRegisterService.addRegister(username));
    }
    /**
     * @Author sjj
     * @Description //TODO 结束签到接口
     * @Date 2021/11/28 2021/11/28
     * @Param [username]
     * @return com.sjj.taskmanagement.common.entities.ResultBody
     */

    @PutMapping("/finishregister")
    public ResultBody finishRegister(@RequestParam("username") String username)
    {
        return ResultBody.success(sysRegisterService.finishRegister(username));
    }
    /**
     * @Author sjj
     * @Description //TODO 获取签到列表或者个人签到信息
     * @Date 2021/11/28 2021/11/28
     * @Param [username]
     * @return com.sjj.taskmanagement.common.entities.ResultBody
     */

    @GetMapping("/list")
    public ResultBody list(@RequestParam(value = "username",required = false)String username)
    {
        return ResultBody.success(sysRegisterService.getRegister(username));
    }
    /**
     * @Author sjj
     * @Description //TODO 强制签退所有人（当次签到没有时间）
     * @Date 2021/12/1 2021/12/1
     * @Param []
     * @return com.sjj.taskmanagement.common.entities.ResultBody
     */

     @GetMapping("/finishall")
     public ResultBody finishAll()
     {
         return ResultBody.success(sysRegisterService.finishAllRegister());
     }
     /**
      * @Author sjj
      * @Description //TODO 清空所有人签到时长
      * @Date 2021/12/1 2021/12/1
      * @Param []
      * @return com.sjj.taskmanagement.common.entities.ResultBody
      */

     @GetMapping("/clear")
    public ResultBody clear()
     {
         return ResultBody.success(sysRegisterService.clearAllRegister());
     }
     /**
      * @Author sjj
      * @Description //TODO 发送签到时长
      * @Date 2021/12/1 2021/12/1
      * @Param [receivers]
      * @return com.sjj.taskmanagement.common.entities.ResultBody
      */

     @GetMapping("/email")
     public ResultBody email(@RequestParam("receivers") String[] receivers)
     {
         return ResultBody.success(sysRegisterService.sendResult(receivers));
     }
     /**
      * @Author sjj
      * @Description //TODO 举报
      * @Date 2021/12/1 2021/12/1
      * @Param [username]
      * @return com.sjj.taskmanagement.common.entities.ResultBody
      */
    @Transactional//开启事务
    @PostMapping("/report")
    public ResultBody report(@RequestParam("username") String username)
    {
        return ResultBody.success(sysRegisterService.report(username));
    }
    /**
     * @Author sjj
     * @Description //TODO 获取正在签到的列表
     * @Date 2021/12/1 2021/12/1
     * @Param []
     * @return com.sjj.taskmanagement.common.entities.ResultBody
     */

    @GetMapping("registering")
    public ResultBody registering()
    {
        return ResultBody.success(sysRegisterService.getRegisteringList());
    }

    @GetMapping("/dead")
    public ResultBody dead()
    {
        return ResultBody.success(sysRegisterService.dead());
    }

    @GetMapping("/{id}")
    public ResultBody listDirection(@PathVariable("id") Integer id) {
        List<SysRegister> sysRegisters = sysRegisterService.listSpecifiedDirection(id);
        return ResultBody.success(sysRegisters);
    }

}
