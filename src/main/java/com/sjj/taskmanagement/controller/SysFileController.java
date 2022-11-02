package com.sjj.taskmanagement.controller;


import cn.hutool.core.util.ZipUtil;
import com.sjj.taskmanagement.common.entities.ResultBody;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author sjj
 * @since 2021-10-19
 */

@Api(tags = "文件接口")
@RestController
@RequestMapping("/sys/file")
public class SysFileController extends BaseController {

    @PostMapping("/upload")
    @Transactional
    public ResultBody upload(@RequestParam("uploadFile") MultipartFile zipFile, String username, String filename,int tid) throws IOException, ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return sysFileService.uploadFile(zipFile,username,filename,tid);
    }
    @PreAuthorize("hasRole('admin')")
    @ApiOperation(value = "根据tid获取任务文件")
    @GetMapping("/getZipFile")
    public ResultBody getTaskFile(@RequestParam("tid")int tid, HttpServletResponse response) throws Exception {
        String[] fileListByTid = sysFileService.getFileListByTid(tid);
        InputStream[] files = sysFileService.getFiles(fileListByTid);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("任务:"+tid+"文件.zip", "UTF-8"));
        ZipUtil.zip(response.getOutputStream(), fileListByTid, files);
        return ResultBody.success("任务:"+tid+"文件.zip");
    }
    @ApiOperation(value = "获取登陆界面的背景")
    @GetMapping("/loginbg")
    public ResultBody loginBG() throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResultBody.success(sysFileService.getLoginBG());
    }
    @Transactional//开启事务
    @ApiOperation(value = "获取主界面背景")
    @GetMapping("/homebg")
    public ResultBody homeBG() throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResultBody.success(sysFileService.getHomeBG());
    }

}
