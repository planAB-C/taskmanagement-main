package com.sjj.taskmanagement.mapper;

import com.sjj.taskmanagement.common.entities.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sjj
 * @since 2021-10-17
 */

public interface SysUserMapper extends BaseMapper<SysUser> {

    List<Long> getNavMenuIds(Long userId);
    List<SysUser> listByMenuId(Long menuId);
    List<String> getUsernameList();
    List<SysUser> getTimeFalse(Double time);
    List<String> getAdminUserEmail();
}
