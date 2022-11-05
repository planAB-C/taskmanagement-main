package com.sjj.taskmanagement.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sjj.taskmanagement.common.dto.MyPage;
import com.sjj.taskmanagement.common.dto.PageDto;
import com.sjj.taskmanagement.common.entities.ResultBody;
import com.sjj.taskmanagement.common.entities.SysTask;
import com.sjj.taskmanagement.common.entities.SysUser;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sjj
 * @since 2021-10-21
 */
@Api(tags = "任务接口")
@RestController
@RequestMapping("/sys/task")
public class SysTaskController extends BaseController {
    /**
     * @Author sjj
     * @Description //TODO 增加一个任务
     * @Date 2021/10/21 2021/10/21
     * @Param [name, detail, time]
     * @return com.sjj.taskmanagement.common.entities.ResultBody
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/add")
    @Transactional//开启事务
    public ResultBody add(@RequestParam("name") String name, @RequestParam("detail") String detail, @RequestParam("time") int time) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        SysTask sysTask=new SysTask();
        sysTask.setName(name)
                .setDetail(detail)
                .setTime(time);
        sysTaskService.SaveTask(sysTask);
        return ResultBody.success();
    }
    /**
     * @Author sjj
     * @Description //TODO 获取任务列表
     * @Date 2021/10/21 2021/10/21
     * @Param []
     * @return java.util.List<com.sjj.taskmanagement.common.entities.SysTask>
     */

    @GetMapping("/list")
    public IPage<SysTask> list(@RequestParam("page") int page, @RequestParam("limit") int limit)
    {
        MyPage<SysTask> myPage = new MyPage<SysTask>(page, limit);
        return sysTaskService.GetTaskList(myPage);
    }

    /**
     * @Author sjj
     * @Description //TODO 更新任务
     * @Date 2021/10/21 2021/10/21
     * @Param [id, name, detail, time]
     * @return com.sjj.taskmanagement.common.entities.ResultBody
     */
    @PreAuthorize("hasRole('admin')")
    @PostMapping("/update")
    @Transactional//开启事务
    public ResultBody update(@RequestParam("id") Long id,@RequestParam("name") String name, @RequestParam("detail") String detail, @RequestParam("time") int time)
    {
        SysTask sysTask=new SysTask();
        sysTask.setId(id);
        sysTask.setName(name)
                .setDetail(detail)
                .setTime(time);
        sysTaskService.UpdateTask(sysTask);
        return ResultBody.success();
    }
    /**
     * @Author sjj
     * @Description //TODO 根据任务id获得任务详情
     * @Date 2021/10/21 2021/10/21
     * @Param [id]
     * @return com.sjj.taskmanagement.common.entities.SysTask
     */

    @GetMapping("/getbyid")
    public SysTask getbyid(@RequestParam("id") int id)
    {
        return sysTaskService.GetById(id);
    }
    /**
     * @Author sjj
     * @Description //TODO 获取完成人员的列表
     * @Date 2021/10/21 2021/10/21
     * @Param [id]
     * @return java.util.List<com.sjj.taskmanagement.common.entities.SysUser>
     */
    @ApiOperation(value = "获取某个任务的已完成名单（管理员）")
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/finished")
    public PageDto getFinishedList(@RequestParam("page") int page,
                                   @RequestParam("limit") int limit,
                                   @RequestParam(value = "id",required = false,defaultValue = "-1") int id)
    {
        return sysTaskService.GetFinishedList(id,page,limit);
    }
    /**
     * @Author sjj
     * @Description //TODO 获取未完成任务的人员列表
     * @Date 2021/10/21 2021/10/21
     * @Param [id]
     * @return java.util.List<com.sjj.taskmanagement.common.entities.SysUser>
     */
    @ApiOperation(value = "获取某个任务的未完成名单（管理员）")
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/unfinished")
    public PageDto getUnfinishedList(@RequestParam(value = "page",required = false,defaultValue = "-1") int page,@RequestParam(value = "limit",required = false,defaultValue = "-1") int limit,@RequestParam(value = "id",required = false,defaultValue = "-1") int id)
    {
        return sysTaskService.GetunFinishedList(id,page,limit);
    }


    /**
     * @Author yyl
     * @Description //TODO 逻辑删除
     * @Date 2021/10/21 2021/10/21
     * @Param [id]
     * @return
     */
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "逻辑删除任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "要删除的任务id", required = true, dataType = "int"),
    })
    @DeleteMapping("/delete")
    public void deleteLogic(@RequestParam("id") int id){
        recyclerService.deleteTaskLogic(id);
    }


    /**
     * @Author yyl
     * @Description //TODO 逻辑恢复
     * @Date 2021/10/21 2021/10/21
     * @Param [id]
     * @return
     */
    @ApiOperation(value = "复原被逻辑删除的任务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "任务id", required = true, dataType = "int"),
    })
    @PutMapping("/undelete")
    public void undeleteTaskLogic(@RequestParam("id") int id){
        recyclerService.undeleteTaskLogic(id);
    }
    /**
     * @Author sjj
     * @Description //TODO 获取用户的任务列表
     * @Date 2021/10/27 2021/10/27
     * @Param [username, page, limit]
     * @return com.sjj.taskmanagement.common.dto.PageDto
     */

    @GetMapping("/getlistbyusername")
    public PageDto getListByUsername(String username,int page,int limit)
    {
        return sysTaskService.GetListByUsername(username,page,limit);
    }
    /**
     * @Author sjj
     * @Description //TODO 获取用户已完成的列表
     * @Date 2021/10/27 2021/10/27
     * @Param [username, page, limit]
     * @return com.sjj.taskmanagement.common.dto.PageDto
     */

    @GetMapping("/getfinishedlistbyusername")
    public PageDto getFinishedListByUsername(String username,int page,int limit)
    {
        return sysTaskService.GetFinishedListByUsername(username,page,limit);
    }
    /**
     * @Author sjj
     * @Description //TODO 获取用户未完成的列表
     * @Date 2021/10/27 2021/10/27
     * @Param [username, page, limit]
     * @return com.sjj.taskmanagement.common.dto.PageDto
     */

    @GetMapping("/getunfinishedlistbyusername")
    public PageDto getUnfinishedListByUsername(String username,int page,int limit)
    {
        return sysTaskService.GetUnfinishedListByUsername(username,page,limit);
    }
    /**
     *
     * @description 根据任务id返回任务列表
     * <br/>
     * @param id
     * @param page
     * @param limit
     * @return com.sjj.taskmanagement.common.entities.ResultBody
     * @author sjj
     * @date 2022/1/10 22:48
     */
    @ApiOperation(value = "根据方向分类获取任务列表（管理员）")
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/getlistbydirection")
    public ResultBody getListByDirectionId(int id,int page,int limit){
        return ResultBody.success(sysTaskService.getTaskListbyDirectionId(id,page,limit));
    }

    @ApiOperation(value = "获取某用户任务是否完成")
    @GetMapping("/isfinish")
    public ResultBody isFinish(int id)
    {
        return ResultBody.success(sysTaskService.getIsFinish(id));
    }
}
