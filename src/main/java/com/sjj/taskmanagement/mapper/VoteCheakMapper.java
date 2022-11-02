package com.sjj.taskmanagement.mapper;

import com.sjj.taskmanagement.common.entities.Vote;
import com.sjj.taskmanagement.common.entities.VoteCheak;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoteCheakMapper {

    /**
     * 插入备选人员和投票人信息
     * @param voteCheak 人员信息
     * @return 受影响的行数
     */
    Integer insert(VoteCheak voteCheak);

    /**
     * 查票
     * @param alternativeName 备选人
     * @return  人
     */
    int findByCheakid(String alternativeName);

    int cheak(String alternativeName,String voter);
}
