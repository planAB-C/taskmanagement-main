package com.sjj.taskmanagement.common.entities;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author sjj
 * @since 2021-10-31
 */
@Data
@Accessors(chain = true)
public class SysFinished {

    private static final long serialVersionUID = 1L;

    /**
     * 任务的名称
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String username;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updated;

}
