package com.leyou.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 下午6:38
 * @Since 1.0.0
 */


public interface UploadService {

    /**
     * 上传图片，返回图片访问地址
     * @param file
     * @return
     */
    String uploadImage(MultipartFile file);
}