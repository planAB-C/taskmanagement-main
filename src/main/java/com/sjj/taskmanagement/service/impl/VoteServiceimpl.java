package com.sjj.taskmanagement.service.impl;

import com.sjj.taskmanagement.common.entities.Vote;
import com.sjj.taskmanagement.common.entities.VoteCheak;
import com.sjj.taskmanagement.common.errorHandler.BizException;
import com.sjj.taskmanagement.mapper.VoteCheakMapper;
import com.sjj.taskmanagement.mapper.VoteMapper;
import com.sjj.taskmanagement.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/** 新增*/
@Service
public class VoteServiceimpl implements VoteService {
    @Autowired
    private VoteMapper voteMapper;
    @Autowired
    private VoteCheakMapper voteCheakMapper;
    /**新增*/
    @Override
    public void addVote(String alternativeName) {
        Vote result = voteMapper.findByid(alternativeName);
        //Date date = new Date();
        if(result == null){//没有加入，新增操作
            Vote vote = new Vote();
            //vote.setId(id);
            vote.setVname(alternativeName);
            //vote.setVnums(1);

            Integer rows = voteMapper.insert(vote);
            if ( rows != 1){
                throw new BizException("插入数据产生未知的异常");
            }
        }else {

                throw new BizException("用户已存在");

        }

    }
    /**查票*/
    @Override
    public void Nums(String alternativeName){
        int result = voteCheakMapper.findByCheakid(alternativeName);
//        if (result == null){
//            throw new BizException("用户不存在");
//        }else {
//            Integer newnums = result.getVnums()+1;
//            Integer rows = voteMapper.updateNumsById(result.getId(),newnums);
//            if (rows != 1){
//                throw new BizException("更新时出现异常");
//            }
//        }
//        Vote resultname = voteMapper.findByid(alternativeName);
        voteMapper.updateNumsById(alternativeName,result);
    }

    @Override
    public void cheakVote(String alternativeName,String voter){
        int resultcheak = voteCheakMapper.cheak(alternativeName,voter);
        if (resultcheak >= 1){
            throw new BizException("用户已投");
        }else {
//                Vote result = voteMapper.findByid(alternativeName);
//                if (result == null){
//                throw new BizException("用户不存在");
//                }else {
//                    //获取当前用户名
//                    String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//                    boolean cheakflag = true;
//                    /*查重，cheakflag*/
//                    if(cheakflag){
//                        addNums(id);//调用自己
//                        VoteCheak voteCheak = new VoteCheak();
//                        voteCheak.setVoter(username);
//                        voteCheak.setAlternativeName(result.getVname());
//                    }
//
//            }
            VoteCheak voteCheak = new VoteCheak();
            voteCheak.setAlternativeName(alternativeName);
            voteCheak.setVoter(voter);
            voteCheakMapper.insert(voteCheak);
        }

    }
    @Override
    public List<Vote> find(){

        return voteMapper.findall();
    }
}
