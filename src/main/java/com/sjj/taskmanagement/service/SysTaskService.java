package com.sjj.taskmanagement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjj.taskmanagement.common.dto.PageDto;
import com.sjj.taskmanagement.common.entities.SysTask;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sjj.taskmanagement.common.entities.SysUser;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sjj
 * @since 2021-10-21
 */
public interface SysTaskService extends IService<SysTask> {
    void SaveTask(SysTask sysTask) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    IPage<SysTask> GetTaskList(Page page);
    void UpdateTask(SysTask sysTask);
    PageDto GetFinishedList(int id,int page, int limit);
    PageDto GetunFinishedList(int id,int page, int limit);
    SysTask GetById(int id);
    PageDto GetListByUsername(String username,int page,int limit);
    PageDto GetFinishedListByUsername(String username,int page,int limit);
    PageDto GetUnfinishedListByUsername(String username,int page,int limit);
    /**
     *
     * @description 根据方向id返回任务列表
     * <br/>
     * @param id
     * @param page
     * @param limit
     * @return com.sjj.taskmanagement.common.dto.PageDto
     * @author sjj
     * @date 2022/1/10 22:45
     */
    PageDto getTaskListbyDirectionId(int id,int page,int limit);
    /**
     *
     * @description 查询当前用户某个任务是否完成
     * <br/>
     * @param id
     * @return boolean
     * @author sjj
     * @date 2022/1/10 22:57
     */
    boolean getIsFinish(int id);
}
