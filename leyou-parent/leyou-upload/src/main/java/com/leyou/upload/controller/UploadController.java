package com.leyou.upload.controller;

import com.leyou.upload.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author cloudlandboy
 * @Date 2019/12/4 下午6:33
 * @Since 1.0.0
 */

@Controller
@RequestMapping("upload")
public class UploadController {
    @Autowired
    private UploadService uploadService;

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String saveUrl = uploadService.uploadImage(file);
        if (saveUrl == null) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity(saveUrl, HttpStatus.CREATED);
    }
}