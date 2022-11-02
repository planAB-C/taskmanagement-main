package com.sjj.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjj.taskmanagement.common.dto.PageDto;
import com.sjj.taskmanagement.common.entities.SysTask;
import com.sjj.taskmanagement.mapper.SysTaskMapper;
import com.sjj.taskmanagement.service.RecyclerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

@Slf4j
@Service
public class RecyclerServiceImpl extends ServiceImpl<SysTaskMapper, SysTask>  implements RecyclerService {

    /**
     * 逻辑删除任务
     * @A
     * @param id
     */
    @Override
    public void deleteTaskLogic(int id) {
        boolean b = update(null, new UpdateWrapper<SysTask>()
                .set("deleted",true)
                .eq("deleted", false)
                .eq("id",id)
        );
    }

    /**
     * 逻辑恢复任务
     *
     * @param id
     */
    @Override
    public void undeleteTaskLogic(int id) {
        boolean b = update(null, new UpdateWrapper<SysTask>()
                .set("deleted",false)
                .eq("deleted", true)
                .eq("id",id)
        );
    }

    /**
     * 获取已经被删除的任务(加入回收站的)
     *
     * @return
     */
    @Override
    public PageDto getDeletedTaskList(int page,int limit) {
        QueryWrapper<SysTask> wrapper=new QueryWrapper<SysTask>()
                .eq("deleted", true);
        Page<SysTask> taskPage =new Page<>(page,limit);
        IPage<SysTask> taskIPage= page(taskPage,wrapper);
        List <SysTask> list=taskIPage.getRecords();
        for (SysTask sysTask : list) {
            //计算出各个任务的deadline
            sysTask.setDeadline(getDeadline(sysTask.getUpdated(),sysTask.getTime()));
        }
        PageDto pageDto=new PageDto();
        pageDto.setList(list)
                .setCount(taskIPage.getTotal())
                .setPages(taskIPage.getPages())
                .setPageNum(page);
        return pageDto;
    }




    private LocalDateTime getDeadline(LocalDateTime time, Integer durtime)
    {
        Long deadline= time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()+durtime*1000*60;
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(deadline), TimeZone.getDefault().toZoneId());
    }
}
