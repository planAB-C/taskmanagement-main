package com.sjj.taskmanagement.common.entities;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {

	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime created;
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updated;

	private Integer statu;
}
