package com.sjj.taskmanagement.common.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** 投票系统实体类 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class VoteCheak extends BaseEntity{
    private String alternativeName;
    private String voter;
}
