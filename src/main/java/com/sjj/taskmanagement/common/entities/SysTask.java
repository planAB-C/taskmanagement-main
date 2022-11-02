package com.sjj.taskmanagement.common.entities;


import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author sjj
 * @since 2021-10-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)//开启链式编程
public class SysTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String name;

    private String detail;

    private Integer time;

    private String flag;
    @TableField(exist = false)
    private LocalDateTime deadline;

    /**
     * 是否被删除 1为被删除 0为未被删除
     */
    private Boolean deleted;
    //方向id
    private Integer directionId;
    //头像url
    private String avatar;
    //是否以及完成
    @TableField(exist = false)
    private Boolean isFinished;


}
