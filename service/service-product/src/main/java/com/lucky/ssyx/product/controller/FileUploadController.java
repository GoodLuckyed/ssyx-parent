package com.lucky.ssyx.product.controller;


import com.lucky.ssyx.common.result.Result;
import com.lucky.ssyx.product.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lucky
 * @date 2023/9/4
 */

@Api(tags = "文件上传接口")
@RestController
@RequestMapping("/admin/product")
@CrossOrigin
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    @ApiOperation("图片上传")
    @PostMapping("/fileUpload")
    public Result fileUpload(MultipartFile file) throws Exception {
        String url = fileUploadService.fileUpload(file);
        return Result.ok(url);
    }
}
