package com.lucky.ssyx.product.service;

import com.aliyuncs.exceptions.ClientException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lucky
 * @date 2023/9/4
 */
public interface FileUploadService {

    /**
     * 图片上传
     * @param file
     * @return
     */
    String fileUpload(MultipartFile file) throws Exception;
}
