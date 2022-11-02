package com.sjj.taskmanagement.common.entities;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author sjj
 * @since 2021-10-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SysFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String url;

    private String username;

    private Integer tid;


}
