package com.sjj.taskmanagement.controller;

import cn.hutool.core.util.ZipUtil;
import com.sjj.taskmanagement.common.entities.ResultBody;
import com.sjj.taskmanagement.common.entities.SysUser;
import com.sjj.taskmanagement.service.DelayedQueueService;
import com.sjj.taskmanagement.service.SysUserService;

import com.sjj.taskmanagement.utils.MinioUtils;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author sjj
 * @Description //TODO 测试controller
 * @Date 2021/10/17 2021/10/17
 */

@Api(tags = "测试用接口")
@RestController
public class TestController extends BaseController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/test")
    public ResultBody test() {
        return ResultBody.success(sysUserService.list());
    }
    @PreAuthorize("hasAuthority('sys:user:list')")
    @GetMapping("/test/pass")
    public ResultBody pass() {

        // 加密后密码
        String password = bCryptPasswordEncoder.encode("111111");

        boolean matches = bCryptPasswordEncoder.matches("111111", password);

        System.out.println("匹配结果：" + matches);

        return ResultBody.success(password);
    }
    @GetMapping("/delay")
    public ResultBody delay(String id)
    {

        //delayedQueueService.sendMsg(id,1);
        return ResultBody.success();
    }


    @PostMapping("/upload")
    @Transactional
    public void upload(@RequestParam("uploadFile") MultipartFile zipFile, String username, String filename, int tid) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        System.out.println(zipFile.getName());
        System.out.println(sysFileService.getFileSuffix(zipFile));
        System.out.println(zipFile.getContentType());
        minioUtils.putObject(minioUtils.getBucketName(),zipFile,"text/"+filename,"multipart/form-data") ;
    }

    @GetMapping("/download")
    @Transactional
    public void download(HttpServletResponse response) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InvalidBucketNameException, InsufficientDataException, InternalException {
        InputStream object = minioUtils.getObject(minioUtils.getBucketName(), "z2.png");
        InputStream in = null;
        OutputStream out = null;
        try {
            in = minioUtils.getObject(minioUtils.getBucketName(), "z2.png");
            int len = 0;
            byte[] buffer = new byte[1024];
            out = response.getOutputStream();
            response.reset();
            response.addHeader("Content-Disposition",
                    " attachment;filename=" + new String("z2.png".getBytes(),"iso-8859-1"));
            response.setContentType("application/octet-stream");
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
    @GetMapping("/mqtt")
    public ResultBody publishTopic() {
       /// mqttPushClient.publish(0,false,"test/1","测试一下发布消息");
        return ResultBody.success();
    }
    @ApiOperation(value = "根据tid获取任务文件")
    @GetMapping("/getZipFile")
    public void getTaskFile(@RequestParam("tid")int tid, HttpServletResponse response) throws Exception {
        String[] fileListByTid = sysFileService.getFileListByTid(tid);
        InputStream[] files = sysFileService.getFiles(fileListByTid);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("任务:"+tid+"文件.zip", "UTF-8"));
        ZipUtil.zip(response.getOutputStream(), fileListByTid, files);

    }

}
