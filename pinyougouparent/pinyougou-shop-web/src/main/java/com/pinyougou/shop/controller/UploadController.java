package com.pinyougou.shop.controller;

import com.pinyougou.fastdfs.UploadUtil;
import com.pinyougou.http.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class UploadController {

    @Value("${tracker.conf}")
    private String trackerConf;

    @Value("${tracker.domain}")
    private String trackerDomain;

    /**
     * 文件上传
     * @param file 前台名称与后台保持一致
     * @return
     */
    @RequestMapping("/upload.do")
    public Result upload(MultipartFile file) throws Exception {
        String originalFileName = file.getOriginalFilename();
        String suffix = StringUtils.getFilenameExtension(originalFileName);

        //FastDFS
        String[] returnValues = UploadUtil.upload(trackerConf, file.getBytes(), suffix);

        //图片访问地址
        String url = trackerDomain + "/" + returnValues[0] + "/" + returnValues[1];
        return new Result(true, url);
    }
}
