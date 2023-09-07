package com.lucky.ssyx.product.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import com.lucky.ssyx.product.service.FileUploadService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.dc.pr.PRError;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

/**
 * @author lucky
 * @date 2023/9/4
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

    @Value("${aliyun.endpoint}")
    private String endPoint;
    @Value("${aliyun.OSS_ACCESS_KEY_ID}")
    private String OSS_ACCESS_KEY_ID;
    @Value("${aliyun.OSS_ACCESS_KEY_SECRET}")
    private String OSS_ACCESS_KEY_SECRET;
    @Value("${aliyun.bucketname}")
    private String bucketName;

    /**
     * 图片上传
     * @param file
     * @return
     */
    @Override
    public String fileUpload(MultipartFile file) throws Exception {
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        objectName = uuid + objectName;

        //按照当前日期，创建文件夹，上传到创建文件夹里面
        //  2023/02/02/01.jpg
        String currentTime = new DateTime().toString("yyyy/MM/dd");
        objectName = currentTime + "/" + objectName;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint,OSS_ACCESS_KEY_ID,OSS_ACCESS_KEY_SECRET);
        try {
            InputStream inputStream = file.getInputStream();
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            // 关闭OSSClient。
            ossClient.shutdown();
            String url = "https://"+bucketName+"."+endPoint+"/"+objectName;
            return url;
        } catch (IOException oe) {
            oe.printStackTrace();
        }
        return null;
    }
}
