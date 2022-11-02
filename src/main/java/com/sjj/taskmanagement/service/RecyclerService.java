package com.sjj.taskmanagement.service;


import com.sjj.taskmanagement.common.dto.PageDto;
import com.sjj.taskmanagement.common.entities.SysTask;

import java.util.List;


public interface RecyclerService {
    /**
     * @Author yyl
     * @Description //TODO 逻辑删除任务
     * @Date 2021/10/21 2021/10/21
     * @Param [id]
     * @return
     */
    void deleteTaskLogic(int id);


    /**
     * @Author yyl
     * @Description //TODO 逻辑恢复任务
     * @Date 2021/10/21 2021/10/21
     * @Param [id]
     * @return
     */
    void undeleteTaskLogic(int id);

    /**
     * @Author yyl
     * @Description //TODO 获取所有被逻辑删除的任务
     * @Date 2021/10/21 2021/10/21
     * @return List<SysTask>
     */
    PageDto getDeletedTaskList(int page, int limit);




}
