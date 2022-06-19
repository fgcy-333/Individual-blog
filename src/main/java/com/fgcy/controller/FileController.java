package com.fgcy.controller;

import com.fgcy.util.COSClientUtil;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author fgcy
 * @Date 2022/5/25
 */
@RestController
public class FileController {
    @PostMapping("/image/upload")
    public Map uploadFileResult(@RequestParam(value = "editormd-image-file", required = false) MultipartFile image) throws Exception {
        Assert.notNull(image, "图片不存在");
        String path = COSClientUtil.upload(image);
        Map resultMap = new HashMap<>();
        resultMap.put("success", 1);
        resultMap.put("message", "成功");
        resultMap.put("url", path);
        return resultMap;
    }
}
