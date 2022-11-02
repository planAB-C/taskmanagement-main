package com.sjj.taskmanagement.common.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
/**
 *
 * @description 分页dto新版
 * @author sjj
 * @date 2022/1/3 14:01
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class MyPage<T> extends Page<T> {
    private static final long serialVersionUID = 5194933845448697148L;

    private Integer selectInt;
    private String selectStr;

    public MyPage(long current, long size) {
        super(current, size);
    }
}
