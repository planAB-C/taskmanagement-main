package com.sjj.taskmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sjj.taskmanagement.common.entities.*;
import com.sjj.taskmanagement.common.errorHandler.BizException;
import com.sjj.taskmanagement.mapper.SysFileMapper;
import com.sjj.taskmanagement.service.SysFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sjj.taskmanagement.service.SysRegisterService;
import com.sjj.taskmanagement.service.SysTaskService;
import com.sjj.taskmanagement.utils.MinioUtils;
import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author sjj
 * @since 2021-10-19
 */
@Service
@Slf4j
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private SysTaskService sysTaskService;
    @Autowired
    private SysRegisterService sysRegisterService;
    @Override
    public SysFile getbyuserusernameandtid(String username,int tid) {
        return getOne(new QueryWrapper<SysFile>().eq("username",username).eq("tid",tid));
    }

    @Override
    public boolean savefile(SysFile sysFile) {
        return save(sysFile);
    }
    /**
     * @Author sjj
     * @Description //TODO 上传接口
     * @Date 2021/10/18 2021/10/18
     * @Param [zipFile 需要上传的文件, username 用户名, filename 文件名]
     * @return com.sjj.taskmanagement.common.entities.ResultBody
     */
    @Override
    public ResultBody uploadFile(MultipartFile zipFile, String username, String filename,int tid) throws IOException, ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        SysFile sysFile= getbyuserusernameandtid(username,tid);
        SysTask sysTask= sysTaskService.getById(tid);
        if(sysTask.getStatu()==0)
        {
            return ResultBody.error("任务已经超时,不允许上传！");
        }
        else if(sysTask.getDeleted()==true)
        {
            return ResultBody.error("任务已删除,不允许上传！");
        }
        //文件保存的路径
//        String targetFilePath = "/www/text";
//        String fileName = UUID.randomUUID().toString().replace("-", "");
        String fileSuffix = getFileSuffix(zipFile);
        if (fileSuffix != null) {   // 拼接后缀
            filename += fileSuffix;
        }
        //将文件存入minio之中
        minioUtils.putObject(minioUtils.getBucketName(),zipFile,String.valueOf(tid)+"/"+filename,"multipart/form-data") ;
//        File targetFile = new File(targetFilePath + File.separator + fileName);
//        File file1 = new File(targetFilePath);
//        if (!file1.exists()) {
//            file1.mkdirs();
//        }
//        //把图片存储到服务器中
//        zipFile.transferTo(targetFile);
        log.info("{}上传成功！",filename);
        //将文件url存到数据库
        String url=String.valueOf(tid)+"/"+filename;
        if(sysFile==null)
        {
            sysFile=new SysFile();
            //如果数据库中没有则插入
            sysFile.setUsername(username);
            sysFile.setTid(tid);
            sysFile.setUrl(url);
            sysFile.setStatu(Constraint.STATUS_OFF);
            sysFile.setName(filename);
            save(sysFile);
        }
        else
        {
            //有数据就更新
            sysFile.setUrl(url)
                    .setName(filename);
            update(sysFile,new QueryWrapper<SysFile>().eq("username",username).eq("tid",tid));
        }
        return ResultBody.success();
    }
    /**
     * @Author sjj
     * @Description //TODO 获得文件的后缀
     * @Date 2021/10/18 2021/10/18
     * @Param [file 文件]
     * @return java.lang.String
     */

    //获取文件的后缀
    @Override
    public String getFileSuffix(MultipartFile file) {
        if (file == null) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        int suffixIndex = fileName.lastIndexOf(".");
        if (suffixIndex == -1) {    // 无后缀
            return null;
        } else {                    // 存在后缀
            return fileName.substring(suffixIndex, fileName.length());
        }
    }

    /**
     * 根据任务id获取对应的提交文件列表
     * @param Tid 任务id
     * @return
     */
    @Override
    public String[] getFileListByTid(int Tid) {
        Iterable<Result<Item>> results = minioUtils.listObjects(minioUtils.getBucketName(), String.valueOf(Tid), true);
        ArrayList<String> res = new ArrayList<>();
        results.forEach(
                itemResult -> {
            try {
                res.add(URLDecoder.decode(itemResult.get().objectName(),"utf-8"));
            } catch (Exception E){

            }
        });
        return res.toArray(new String[res.size()]);
    }

    @Override
    public InputStream[] getFiles(String[] filelist) throws Exception{
        ArrayList<InputStream> files = new ArrayList<>();
        for (String s : filelist) {
            InputStream stream = minioUtils.getObject(minioUtils.getBucketName(), s);
            files.add(stream);
        }
        return files.toArray(new InputStream[filelist.length]);
    }

    @Override
    public String getLoginBG() throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String baseUrl="http://47.106.183.36:9000/login/";
        List<String> list=minioUtils.getFilesNameInBucket("login");
        Random random=new Random();
        int index=random.nextInt(list.size());
        //System.out.println(index);
        return baseUrl+list.get(index);
    }

    @Override
    public String getHomeBG() throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String baseUrl="http://47.106.183.36:9000/home/";
        List<String> list=minioUtils.getFilesNameInBucket("home");
        Random random=new Random();
        int index=random.nextInt(list.size());
        //获取当前用户名
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        QueryWrapper<SysRegister> wrapper=new QueryWrapper<>();
        wrapper.eq("username",username);
        SysRegister sysRegister=sysRegisterService.getOne(wrapper);
        if(sysRegister==null)
        {
            SysRegister sysRegister1=new SysRegister();
            sysRegister1.setUsername(username)
                    .setTime(0D)
                    .setStart(LocalDateTime.now(ZoneOffset.of("+8")))
                    .setStatu(0);
            sysRegisterService.save(sysRegister1);
        }
        //System.out.println(index);
        return baseUrl+list.get(index);
    }


}
