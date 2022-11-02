package com.sjj.taskmanagement.controller;

import com.sjj.taskmanagement.common.dto.PageDto;
import com.sjj.taskmanagement.common.entities.SysTask;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "任务回收站接口")
@RestController
@RequestMapping("/sys/recycler")
public class RecyclerController extends BaseController{



    @ApiOperation(value = "获取被逻辑删除的任务")
    @GetMapping("/deletedList")
    public PageDto GetDeletedTaskList(@RequestParam("page") int page, @RequestParam("limit") int limit){
        return recyclerService.getDeletedTaskList(page,limit);
    }



}
