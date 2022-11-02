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
 * @since 2021-10-17
 */
@Data
@Accessors(chain = true)
public class SysUserRole{

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long roleId;


}
