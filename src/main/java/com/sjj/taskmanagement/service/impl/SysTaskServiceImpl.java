package com.sjj.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sjj.taskmanagement.common.dto.PageDto;
import com.sjj.taskmanagement.common.entities.SysFinished;
import com.sjj.taskmanagement.common.entities.SysTask;
import com.sjj.taskmanagement.common.entities.SysUser;
import com.sjj.taskmanagement.mapper.SysTaskMapper;
import com.sjj.taskmanagement.service.DelayedQueueService;
import com.sjj.taskmanagement.service.SysFinishedService;
import com.sjj.taskmanagement.service.SysTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjj.taskmanagement.service.SysUserService;
import com.sjj.taskmanagement.utils.MinioUtils;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sjj
 * @since 2021-10-21
 */
@Service
public class SysTaskServiceImpl extends ServiceImpl<SysTaskMapper, SysTask> implements SysTaskService {
    /**
     * @Author sjj
     * @Description //TODO 保存任务
     * @Date 2021/10/21 2021/10/21
     * @Param
     * @return
     */
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private SysFinishedService sysFinishedService;
    @Autowired
    private SysTaskMapper sysTaskMapper;
    @Override
    public void SaveTask(SysTask sysTask) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        //获取当前用户名
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //获取当前用户
        SysUser sysUser = sysUserService.getByUsername(username);
        sysTask.setAvatar(sysUser.getAvatar());
        sysTask.setDirectionId(sysUser.getDirectionId());
        //初始flag是0用来校验延迟队列消息
        sysTask.setFlag("0");
        //任务的默认状态为未结束
        sysTask.setStatu(1);
        //任务默认未删除
        sysTask.setDeleted(false);
        save(sysTask);
        minioUtils.putDirObject(minioUtils.getBucketName(),String.valueOf(sysTask.getId())+"/");
        //flag使用“，”隔开
        //发送消息到延迟队列
        //delayedQueueService.sendMsg(sysTask.getId().toString()+",0",sysTask.getTime());
    }
    /**
     * @Author sjj
     * @Description //TODO 获得任务列表
     * @Date 2021/10/21 2021/10/21
     * @Param []
     * @return java.util.List<com.sjj.taskmanagement.common.entities.SysTask>
     */

    @Override
    public IPage<SysTask> GetTaskList(Page page) {

//        Page<SysTask> taskPage =new Page<>(page,limit);
//        QueryWrapper<SysTask> wrapper=new QueryWrapper<SysTask>().ne("deleted",true).orderByDesc("id");
//        IPage<SysTask> taskIPage= page(taskPage,wrapper);
//        List <SysTask> sysTaskList=taskIPage.getRecords();
//        for (SysTask sysTask : sysTaskList) {
//            //计算出各个任务的deadline
//            sysTask.setDeadline(getDeadline(sysTask.getUpdated(),sysTask.getTime()));
//        }
//        PageDto taskDto=new PageDto();
//        taskDto.setList(sysTaskList)
//                .setCount(taskIPage.getTotal())
//                .setPages(taskIPage.getPages())
//                .setPageNum(page);
        //获取当前用户名
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        IPage<SysTask> sysTaskIPage=sysTaskMapper.selectTaskPageVo(page,username);
        List<SysTask> sysTaskList=sysTaskIPage.getRecords();
        for (SysTask sysTask : sysTaskList) {
            //计算出各个任务的deadline
            sysTask.setDeadline(getDeadline(sysTask.getUpdated(),sysTask.getTime()));
        }
        return sysTaskIPage;
    }
    /**
     * @Author sjj
     * @Description //TODO 更新任务
     * @Date 2021/10/21 2021/10/21
     * @Param [sysTask]
     * @return void
     */

    @Override
    public void UpdateTask(SysTask sysTask) {
        SysTask oldsysTask =getById(sysTask.getId());
        if(!sysTask.getTime().equals(oldsysTask.getTime()))
        {
            //如果修改了时间就要重新发送消息队列，并且更改flag，避免旧的消息影响
            Long flag=Long.parseLong(oldsysTask.getFlag().trim())+1;
            sysTask.setFlag(flag.toString());
            //发送新的消息队列
            //delayedQueueService.sendMsg(sysTask.getId().toString()+","+sysTask.getFlag(),sysTask.getTime());
        }
        updateById(sysTask);
    }
    /**
     * @Author sjj
     * @Description //TODO 获取已完成人员的名单
     * @Date 2021/10/21 2021/10/21
     * @Param [id]
     * @return java.util.List<com.sjj.taskmanagement.common.entities.SysUser>
     */

    @Override
    public PageDto GetFinishedList(int id,int page, int limit) {
        if(id!=-1)
        {
            Page<SysUser> userPage =new Page<>(page,limit);
            QueryWrapper<SysUser> wrapper=new QueryWrapper<>();
            wrapper.inSql("username","select username from sys_file where tid = "+id);
            IPage<SysUser> userIPage=sysUserService.page(userPage,wrapper);
            List<SysUser> sysUsers= userIPage.getRecords();
            PageDto pageDto=new PageDto();
            pageDto.setList(sysUsers)
                    .setCount(userIPage.getTotal())
                    .setPages(userIPage.getPages())
                    .setPageNum(page);
            return pageDto;
        }
        else
        {
            Page<SysFinished> sysFinishedPage =new Page<>(page,limit);
            QueryWrapper<SysFinished> wrapper=new QueryWrapper<>();
            wrapper.orderByAsc("id");
            IPage<SysFinished> sysFinishedIPage=sysFinishedService.page(sysFinishedPage,wrapper);
            List<SysFinished> sysUsers= sysFinishedIPage.getRecords();
            PageDto pageDto=new PageDto();
            pageDto.setList(sysUsers)
                    .setCount(sysFinishedIPage.getTotal())
                    .setPages(sysFinishedIPage.getPages())
                    .setPageNum(page);
            return pageDto;
        }

    }
    /**
     * @Author sjj
     * @Description //TODO 获取未完成的名单
     * @Date 2021/10/21 2021/10/21
     * @Param [id]
     * @return java.util.List<com.sjj.taskmanagement.common.entities.SysUser>
     */

    @Override
    public PageDto GetunFinishedList(int id,int page, int limit) {
        if(id!=-1)
        {
            Page<SysUser> userPage =new Page<>(page,limit);
            QueryWrapper<SysUser> wrapper=new QueryWrapper<>();
            wrapper.notInSql("username","select username from sys_file where tid = "+id);
            IPage<SysUser> userIPage=sysUserService.page(userPage,wrapper);
            List<SysUser> sysUsers= userIPage.getRecords();
            PageDto pageDto=new PageDto();
            pageDto.setList(sysUsers)
                    .setCount(userIPage.getTotal())
                    .setPages(userIPage.getPages())
                    .setPageNum(page);
            return pageDto;
        }
        else
        {
            QueryWrapper<SysTask> wrapper=new QueryWrapper<SysTask>().ne("deleted",true);
            List <SysTask> sysTaskList=list(wrapper);
            List<SysFinished> sysFinishedList=new ArrayList<SysFinished>();
            for (SysTask sysTask : sysTaskList) {
                Long tid= sysTask.getId();
                QueryWrapper<SysUser> userwrapper=new QueryWrapper<>();
                userwrapper.notInSql("username","select username from sys_file where tid = "+tid);
                List<SysUser> sysUserList=sysUserService.list(userwrapper);
                for (SysUser sysUser : sysUserList) {
                    SysFinished sysFinished=new SysFinished();
                    sysFinished.setId(tid)
                            .setUsername(sysUser.getUsername())
                            .setName(sysTask.getName());
                    sysFinishedList.add(sysFinished);
                }
            }
            PageDto pageDto=new PageDto();
            pageDto.setList(sysFinishedList)
                    .setCount(0L)
                    .setPages(0L)
                    .setPageNum(page);
            return pageDto;
        }
    }

    @Override
    public SysTask GetById(int id) {
        SysTask sysTask = getById(id);
        sysTask.setDeadline(getDeadline(sysTask.getUpdated(),sysTask.getTime()));
        return sysTask;
    }

    @Override
    public PageDto GetListByUsername(String username,int page,int limit) {
        Page<SysTask> taskPage =new Page<>(page,limit);
        QueryWrapper<SysTask> wrapper=new QueryWrapper<SysTask>().ne("deleted",true);
        IPage<SysTask> taskIPage= page(taskPage,wrapper);
        List <SysTask> sysTaskList=taskIPage.getRecords();

        QueryWrapper<SysTask> finishwrapper=new QueryWrapper<SysTask>().inSql("id","select tid from sys_file where username = '"+username+"'");
        List<SysTask> finishedList =list(finishwrapper);
        for (SysTask sysTask : sysTaskList) {
            if (finishedList.contains(sysTask))
            {
                sysTask.setIsFinished(true);
            }
            else
            {
                sysTask.setIsFinished(false);
            }
        }
        for (SysTask sysTask : sysTaskList) {
            //计算出各个任务的deadline
            sysTask.setDeadline(getDeadline(sysTask.getUpdated(),sysTask.getTime()));
        }
        PageDto taskDto=new PageDto();
        taskDto.setList(sysTaskList)
                .setCount(taskIPage.getTotal())
                .setPages(taskIPage.getPages())
                .setPageNum(page);
        return taskDto;
    }

    @Override
    public PageDto GetFinishedListByUsername(String username, int page, int limit) {
        Page<SysTask> taskPage =new Page<>(page,limit);
        QueryWrapper<SysTask> wrapper=new QueryWrapper<SysTask>().ne("deleted",true).inSql("id","select tid from sys_file where username = '"+username+"'");
        IPage<SysTask> taskIPage= page(taskPage,wrapper);
        List <SysTask> sysTaskList=taskIPage.getRecords();
        for (SysTask sysTask : sysTaskList) {
            sysTask.setIsFinished(true);
            //计算出各个任务的deadline
            sysTask.setDeadline(getDeadline(sysTask.getUpdated(),sysTask.getTime()));
        }
        PageDto taskDto=new PageDto();
        taskDto.setList(sysTaskList)
                .setCount(taskIPage.getTotal())
                .setPages(taskIPage.getPages())
                .setPageNum(page);
        return taskDto;
    }

    @Override
    public PageDto GetUnfinishedListByUsername(String username, int page, int limit) {
        Page<SysTask> taskPage =new Page<>(page,limit);
        QueryWrapper<SysTask> wrapper=new QueryWrapper<SysTask>().ne("deleted",true).notInSql("id","select tid from sys_file where username = '"+username+"'");
        IPage<SysTask> taskIPage= page(taskPage,wrapper);
        List <SysTask> sysTaskList=taskIPage.getRecords();
        for (SysTask sysTask : sysTaskList) {
            sysTask.setIsFinished(true);
            //计算出各个任务的deadline
            sysTask.setDeadline(getDeadline(sysTask.getUpdated(),sysTask.getTime()));
        }
        PageDto taskDto=new PageDto();
        taskDto.setList(sysTaskList)
                .setCount(taskIPage.getTotal())
                .setPages(taskIPage.getPages())
                .setPageNum(page);
        return taskDto;
    }

    @Override
    public PageDto getTaskListbyDirectionId(int id, int page, int limit) {
        Page<SysTask> taskPage =new Page<>(page,limit);
        QueryWrapper<SysTask> wrapper=new QueryWrapper<SysTask>().ne("deleted",true).eq("direction_id",id).orderByDesc("created");
        IPage<SysTask> taskIPage= page(taskPage,wrapper);
        List <SysTask> sysTaskList=taskIPage.getRecords();
        for (SysTask sysTask : sysTaskList) {
            sysTask.setIsFinished(true);
            //计算出各个任务的deadline
            sysTask.setDeadline(getDeadline(sysTask.getUpdated(),sysTask.getTime()));
        }
        PageDto taskDto=new PageDto();
        taskDto.setList(sysTaskList)
                .setCount(taskIPage.getTotal())
                .setPages(taskIPage.getPages())
                .setPageNum(page);
        return taskDto;
    }

    @Override
    public boolean getIsFinish(int id) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper<SysFinished> wrapper=new QueryWrapper<>();
        wrapper.eq("id",id).eq("username",username);
        SysFinished sysFinished=sysFinishedService.getOne(wrapper);
        if(sysFinished!=null)
        {
            return true;
        }
        return false;
    }

    /**
     * @Author sjj
     * @Description //TODO 计算出任务的deadline
     * @Date 2021/10/21 2021/10/21
     * @Param [time, durtime]
     * @return java.time.LocalDateTime
     */

    private LocalDateTime getDeadline(LocalDateTime time,Integer durtime)
    {
       Long deadline= time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()+durtime*1000*60;
       return LocalDateTime.ofInstant(Instant.ofEpochMilli(deadline), TimeZone.getDefault().toZoneId());
    }
}
