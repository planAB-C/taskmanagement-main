package com.sjj.taskmanagement.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
    private Long count;
    private Integer pageNum;
    private Long pages;
    private List<T> list;
}
