package com.sjj.taskmanagement.common.entities;


import com.sjj.taskmanagement.common.entities.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author sjj
 * @since 2022-01-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysReport extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 举报时长
     */
    private Double time;

    /**
     * 举报人用户名
     */
    private String reporter;

    /**
     * 被举报人用户名
     */
    private String username;


}
