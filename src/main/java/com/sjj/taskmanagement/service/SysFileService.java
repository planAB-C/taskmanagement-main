package com.sjj.taskmanagement.service;

import com.sjj.taskmanagement.common.entities.ResultBody;
import com.sjj.taskmanagement.common.entities.SysFile;
import com.baomidou.mybatisplus.extension.service.IService;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author sjj
 * @since 2021-10-19
 */
public interface SysFileService extends IService<SysFile> {
    SysFile getbyuserusernameandtid(String username,int tid);
    boolean savefile(SysFile sysFile);
    public ResultBody uploadFile(MultipartFile zipFile, String username, String filename,int tid) throws IOException, ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    public String getFileSuffix(MultipartFile zipFile);

    /**
     * 根据任务id获取对应的提交文件
     * @param Tid 任务id
     * @return 提交文件数组
     */
    public String[] getFileListByTid(int Tid);

    /**
     *
     * @param filelist 任务列表
     * @return
     */
    public InputStream[] getFiles(String[] filelist) throws Exception;
    /**
     *
     * @description 获得登陆背景
     * <br/>
     * @param
     * @return java.lang.String
     * @author sjj
     * @date 2022/1/12 23:14
     */
    String getLoginBG() throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    /**
     *
     * @description 获取主页背景
     * <br/>
     * @param
     * @return java.lang.String
     * @author sjj
     * @date 2022/1/12 23:16
     */
    String getHomeBG() throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
