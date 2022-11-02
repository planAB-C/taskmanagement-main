package com.sjj.taskmanagement.service;

import com.sjj.taskmanagement.common.dto.SysMenuDto;
import com.sjj.taskmanagement.common.entities.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sjj
 * @since 2021-10-17
 */
public interface SysMenuService extends IService<SysMenu> {
    /**
     * @Author sjj
     * @Description //TODO 获取当前用户的菜单列表（树状）
     * @Date 2021/10/18 2021/10/18
     * @Param []
     * @return java.util.List<com.sjj.taskmanagement.common.dto.SysMenuDto>
     */

    List<SysMenuDto> getCurrentUserNav();
    /**
     * @Author sjj
     * @Description //TODO 获取所有的菜单列表（树状）
     * @Date 2021/10/18 2021/10/18
     * @Param []
     * @return java.util.List<com.sjj.taskmanagement.common.entities.SysMenu>
     */

    List<SysMenu> tree();
    /**
     *
     * @description 获取当前用户的角色和权限
     * <br/>
     * @param
     * @return java.util.List<java.lang.String>
     * @author sjj
     * @date 2022/1/17 13:13
     */
    List<String> getCurrentRole();
}
