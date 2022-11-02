package com.sjj.taskmanagement.service;

import com.sjj.taskmanagement.common.entities.Vote;

import java.util.List;

/** 投票系统业务层接口 */
public interface VoteService {
    void addVote(String alternativeName);
    void Nums(String alternativeName);
    void cheakVote(String alternativeName,String voter);
    List<Vote> find();
}
