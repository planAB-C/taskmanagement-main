package com.sjj.taskmanagement.controller;


import cn.hutool.core.map.MapUtil;
import com.sjj.taskmanagement.common.dto.SysMenuDto;
import com.sjj.taskmanagement.common.entities.ResultBody;
import com.sjj.taskmanagement.common.entities.SysUser;
import io.swagger.annotations.Api;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sjj
 * @since 2021-10-17
 */

@Api(tags = "菜单接口")
@RestController
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController {
    /**
     * 用户当前用户的菜单和权限信息
     * @param principal
     * @return
     */
    @GetMapping("/nav")
    public ResultBody nav(Principal principal) {
        SysUser sysUser = sysUserService.getByUsername(principal.getName());

        // 获取权限信息
        String authorityInfo = sysUserService.getUserAuthorityInfo(sysUser.getId());// ROLE_admin,ROLE_normal,sys:user:list,....
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");//将字符串拆分成数组

        // 获取导航栏信息
        List<SysMenuDto> navs = sysMenuService.getCurrentUserNav();

        return ResultBody.success(MapUtil.builder()
                .put("authoritys", authorityInfoArray)
                .put("nav", navs)
                .map()
        );
    }
    @GetMapping("/role")
    public ResultBody role()
    {
        return ResultBody.success(sysMenuService.getCurrentRole());
    }

}
