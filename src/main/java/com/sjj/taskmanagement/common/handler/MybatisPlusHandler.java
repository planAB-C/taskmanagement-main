package com.sjj.taskmanagement.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
@Slf4j
public class MybatisPlusHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info(".......start insert.......");
        strictInsertFill(metaObject,"created", LocalDateTime.class,LocalDateTime.now(ZoneOffset.of("+8")));
        strictInsertFill(metaObject,"updated", LocalDateTime.class,LocalDateTime.now(ZoneOffset.of("+8")));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info(".......start update.......");
        strictUpdateFill(metaObject,"updated", LocalDateTime.class,LocalDateTime.now(ZoneOffset.of("+8")));
    }
}
