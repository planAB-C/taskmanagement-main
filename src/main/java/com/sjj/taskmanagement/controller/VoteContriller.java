package com.sjj.taskmanagement.controller;

import com.sjj.taskmanagement.common.entities.ResultBody;
import com.sjj.taskmanagement.service.VoteService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/vote")
@RestController
public class VoteContriller extends BaseController{
    @Autowired
    private VoteService voteService;
    @ApiOperation(value = "注册投票人")
    @PostMapping ("/addvote")
    public ResultBody addvote(String alternativeName){
        voteService.addVote(alternativeName);
        return ResultBody.success();
    }
    @ApiOperation(value = "投票检查")
    @GetMapping ("/cheakvote")
    public ResultBody cheak(String alternativeName,String voter){
        voteService.cheakVote(alternativeName ,voter);
        return ResultBody.success();
    }
    @ApiOperation(value = "统计票数")
    @PostMapping("/addnums")
    public ResultBody addnums(String alternativeName){
        voteService.Nums(alternativeName);
        return ResultBody.success();
    }
    @ApiOperation(value = "查人")
    @PostMapping("/find")
    public ResultBody find(){

        return ResultBody.success(voteService.find());
    }
}
