package com.sjj.taskmanagement.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjj.taskmanagement.common.entities.SysTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sjj.taskmanagement.common.entities.SysUser;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author sjj
 * @since 2021-10-21
 */
public interface SysTaskMapper extends BaseMapper<SysTask> {
    /**
     *
     * @description 按方向返回任务列表
     * <br/>
     * @param page
     * @param username
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.sjj.taskmanagement.common.entities.SysTask>
     * @author sjj
     * @date 2022/1/3 14:05
     */
    public IPage<SysTask> selectTaskPageVo(Page page,String username);

    public IPage<SysUser> selectUnfinishedName(Page page, Integer tid,Integer did);

    public Integer getDirectionId(Integer id);
}
