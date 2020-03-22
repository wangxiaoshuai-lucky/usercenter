package com.kelab.usercenter.controller;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.kelab.info.base.JsonAndModel;
import com.kelab.info.base.constant.StatusMsgConstant;
import com.kelab.info.context.Context;
import com.kelab.usercenter.config.AppSetting;
import com.kelab.usercenter.result.FileUploadResult;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileUploadController {

    private final FastFileStorageClient storageClient;

    public FileUploadController(FastFileStorageClient storageClient) {
        this.storageClient = storageClient;
    }

    /**
     * 文件上传,返回下载连接
     */
    @PostMapping(value = "/file.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public JsonAndModel upload(Context context, @RequestBody MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (StringUtils.isEmpty(filename) || !isAccessFile(filename)) {
            return JsonAndModel.builder(StatusMsgConstant.INCORRECT_FILE_FORMAT).build();
        }
        // 上传并且生成路径
        StorePath storePath = this.storageClient.uploadFile(
                file.getInputStream(), file.getSize(),
                filename.substring(filename.lastIndexOf(".") + 1), null);
        return JsonAndModel.builder(StatusMsgConstant.SUCCESS)
                .data(new FileUploadResult(filename, storePath.getFullPath()))
                .build();
    }


    /**
     * 检测上传的文件是否合法
     */
    private boolean isAccessFile(String name) {
        if (!name.contains(".")) {
            return false;
        }
        String fileExtend = name.substring(name.lastIndexOf(".") + 1);
        String[] accessFileExtend = AppSetting.accessFileExtend.split(",");
        for (String extend : accessFileExtend) {
            if (fileExtend.equals(extend)) {
                return true;
            }
        }
        return false;
    }
}
