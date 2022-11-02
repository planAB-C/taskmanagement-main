package com.sjj.taskmanagement;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjj.taskmanagement.common.dto.MyPage;
import com.sjj.taskmanagement.common.dto.MyPage;
import com.sjj.taskmanagement.common.entities.*;
import com.sjj.taskmanagement.controller.BaseController;
import com.sjj.taskmanagement.controller.VoteContriller;
import com.sjj.taskmanagement.mapper.SysRegisterMapper;
import com.sjj.taskmanagement.mapper.SysUserMapper;
import com.sjj.taskmanagement.mapper.VoteCheakMapper;
import com.sjj.taskmanagement.mapper.VoteMapper;
import com.sjj.taskmanagement.service.*;
import com.sjj.taskmanagement.utils.MinioUtils;
import com.sjj.taskmanagement.utils.RedisUtil;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class TaskmanagementApplicationTests extends BaseController {
    @Autowired
    SysRegisterService sysRegisterService;
    @Resource
    SysUserMapper sysUserMapper;

    @Test
    public void test() {

    }

}
