package com.sjj.taskmanagement.service;

import com.sjj.taskmanagement.common.entities.SysRegister;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjj.taskmanagement.common.entities.SysUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sjj
 * @since 2021-11-28
 */
public interface SysRegisterService extends IService<SysRegister> {
    /**
     * @Author sjj
     * @Description //TODO 添加一条签到记录
     * @Date 2021/11/28 2021/11/28
     * @Param [username]
     * @return boolean
     */
    public boolean addRegister(String username);
    /**
     * @Author sjj
     * @Description //TODO 结束签到
     * @Date 2021/11/28 2021/11/28
     * @Param [username]
     * @return boolean
     */
    public boolean finishRegister(String username);
    /**
     * @Author sjj
     * @Description //TODO 获得签到信息
     * @Date 2021/11/28 2021/11/28
     * @Param [username]
     * @return java.util.List<com.sjj.taskmanagement.common.entities.SysRegister>
     */

    public List<SysRegister>getRegister(String username);
    /**
     * @Author sjj
     * @Description //TODO 签退所有用户
     * @Date 2021/11/29 2021/11/29
     * @Param []
     * @return boolean
     */

    public boolean finishAllRegister();
    /**
     * @Author sjj
     * @Description //TODO 清除所有用户的时长
     * @Date 2021/11/29 2021/11/29
     * @Param []
     * @return boolean
     */

    public boolean clearAllRegister();
    /**
     * @Author sjj
     * @Description //TODO 发送签到结果（没有达到目标的）邮件
     * @Date 2021/11/29 2021/11/29
     * @Param [receivers]
     * @return boolean
     */

    public boolean sendResult(String[] receivers);
    /**
     * @Author sjj
     * @Description //TODO 清除一个人当此签到时长
     * @Date 2021/12/1 2021/12/1
     * @Param [username]
     * @return boolean
     */

    public boolean clearOne(String username);

    /**
     * @Author sjj
     * @Description //TODO 获取正在签到的列表
     * @Date 2021/12/1 2021/12/1
     * @Param []
     * @return java.util.List<com.sjj.taskmanagement.common.entities.SysRegister>
     */

    /**
     * 查看指定方向的签到情况
     * @param id
     * @return
     */
    public List<SysRegister> listSpecifiedDirection(Integer id) ;

    /**
     * 定位签到
     * @return
     */
    public boolean locationSignIn(String username ,double x ,double y) ;

    public List<SysRegister> getRegisteringList();

    public boolean report(String username);

    public boolean dead();
}
