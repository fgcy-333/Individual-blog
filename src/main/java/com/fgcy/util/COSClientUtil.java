package com.fgcy.util;

import com.fgcy.config.COSConfig;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 腾讯云COS文件上传工具类
 */
public class COSClientUtil {
    static COSConfig cosConfig;
    static COSClient cosClient;

    static {
        cosConfig = SpringUtils.getBean(COSConfig.class, "COSConfig");
        BasicCOSCredentials credentials = new BasicCOSCredentials(cosConfig.getAccessKey(), cosConfig.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(cosConfig.getRegionName()));
        cosClient = new COSClient(credentials, clientConfig);
    }


    private COSClientUtil() {

    }

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static String upload(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String unique = uuid.toString().split("-")[0];
        unique += String.valueOf(System.currentTimeMillis()).substring(7);
        String name = originalFilename.substring(originalFilename.lastIndexOf("."));
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH) + 1;
        String folderName = cosConfig.getFolderPrefix() + "/" + year + "/" + month + "/";

        String key = folderName + unique + name;

        File localFile = null;
        try {
            ///转成临时文件
            localFile = transferToFile(file);
            //上传置腾讯云cos系统
            String filePath = uploadFileToCOS(localFile, key);
            return filePath;
        } catch (Exception e) {
            throw new Exception("文件上传失败");
        } finally {
            //删除临时文件
            localFile.delete();
        }
    }


    /**
     * 用缓冲区来实现这个转换, 即创建临时文件
     * 使用 MultipartFile.transferTo()
     *
     * @param multipartFile
     * @return
     */
    private static File transferToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String prefix = originalFilename.split("\\.")[0];
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用给定的前缀和后缀生成文件名，在默认的临时文件目录中创建一个空文件
        File file = File.createTempFile(prefix, suffix);
        multipartFile.transferTo(file);
        return file;
    }

    /**
     * 上传文件到COS
     *
     * @param localFile
     * @param key
     * @return
     */
    private static String uploadFileToCOS(File localFile, String key) throws InterruptedException {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosConfig.getBucketName(), key, localFile);
        ExecutorService threadPool = Executors.newFixedThreadPool(8);
        // 传入一个threadPool, 若不传入线程池, 默认TransferManager中会生成一个单线程的线程池
        TransferManager transferManager = new TransferManager(cosClient, threadPool);
        // 返回一个异步结果Upload, 可同步的调用waitForUploadResult等待upload结束, 成功返回UploadResult, 失败抛出异常
        Upload upload = transferManager.upload(putObjectRequest);
        UploadResult uploadResult = upload.waitForUploadResult();
        transferManager.shutdownNow();
        cosClient.shutdown();
        String filePath = cosConfig.getBaseUrl() + uploadResult.getKey();
        return filePath;
    }

    public static void main(String[] args) {

/*        UUID uuid = UUID.randomUUID();
        String s = uuid.toString().split("-")[0];
        s += String.valueOf(System.currentTimeMillis()).substring(7);
        System.out.println(s);*/
/*        URL url = COSClient.class.getResource("/application.yml");
        System.out.println(url);
        File file = new File(url.getPath());
        if (file.exists()) {
            System.out.println(file.getAbsolutePath());
        }*/
    }

}
