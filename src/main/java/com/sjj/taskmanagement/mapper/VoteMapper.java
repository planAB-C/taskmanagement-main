package com.sjj.taskmanagement.mapper;

import com.sjj.taskmanagement.common.entities.Vote;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
/** 投票系统持久层接口 */
public interface VoteMapper {
    /**
     * 插入备选人员
     * @param vote 人员信息
     * @return 受影响的行数
     */
    Integer insert(Vote vote);

    /**
     * 票数统计
     * @param vname 备选人
     * @return 当前票数
     */
    Vote findByid(String vname);

    /**
     * 更新票数

     * @param vnums 更新的票数
     * @return 受影响的行数
     */
    Integer updateNumsById(String vname,Integer vnums);//更改信息

    List<Vote> findall ();
}
