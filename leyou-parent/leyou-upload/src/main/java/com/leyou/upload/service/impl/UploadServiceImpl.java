package com.leyou.upload.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.upload.service.UploadService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 下午8:08
 * @Since 1.0.0
 */

@Service
public class UploadServiceImpl implements UploadService {

    @Value("${image_server_domain}")
    String image_server_domain;

    /**
     * 注入文件上传客户端组件
     */
    @Autowired
    private FastFileStorageClient storageClient;

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final List<String> CONTENT_TYPES = Arrays.asList(MimeTypeUtils.IMAGE_JPEG_VALUE, MimeTypeUtils.IMAGE_PNG_VALUE);

    @Override
    public String uploadImage(MultipartFile file) {
        //1. 判断文件类型是否合法
        String contentType = file.getContentType();
        if (!CONTENT_TYPES.contains(contentType)) {
            return null;
        }

        //2. 判断文件内容是否合法
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                //记录日志
                this.LOGGER.warn("文件内容不合法：{}", file.getOriginalFilename());
                return null;
            }

            //获取文件后缀名
            String ext = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), ext, null);
            String savePath = storePath.getFullPath();
            //生成url返回
            return image_server_domain + "/" + savePath;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}