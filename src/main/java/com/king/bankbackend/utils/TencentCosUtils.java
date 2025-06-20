package com.king.bankbackend.utils;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class TencentCosUtils {

    private String secretId;
    private String secretKey;
    private String region;
    private String bucket;
    private String host;

    /**
     * 有参构造函数，用于配置类注入参数
     * @param secretId 密钥ID
     * @param secretKey 密钥Key
     * @param region 地域
     * @param bucket 存储桶名称
     * @param host 基础URL
     */
    public TencentCosUtils(String secretId, String secretKey, String region, String bucket, String host) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.region = region;
        this.bucket = bucket;
        this.host = host;
    }

    public COSClient createCosClient(){
        // 1. 初始化身份信息
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);

        // 2. 设置地域（Region）
        ClientConfig clientConfig = new ClientConfig(new Region(region));

        // 3. 创建客户端
        COSClient cosClient = new COSClient(cred, clientConfig);

        return cosClient;
    }

    /**
     * 上传文件到COS
     * @param file 上传的文件
     * @return 文件访问URL
     */
    public String uploadFile(MultipartFile file) {
        COSClient cosClient = null;
        try {
            // 1. 创建COSClient实例
            cosClient = createCosClient();

            // 2. 准备上传参数
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.lastIndexOf(".") < 0) {
                throw new IllegalArgumentException("文件名不合法");
            }

            String fileExt = originalFilename.substring(originalFilename.lastIndexOf("."));
            String key = UUID.randomUUID() + fileExt;

            // 3. 创建上传请求（使用文件流而非File对象，避免临时文件）
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    bucket,
                    key,
                    file.getInputStream(),
                    metadata
            );

            // 4. 执行上传
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            log.debug("文件上传结果：{}", putObjectResult.getETag());

            // 5. 构建访问URL
            String url = "https://" + bucket + ".cos." + region + ".myqcloud.com/" + key;
            log.info("文件上传成功，URL：{}", url);
            return url;

        } catch (CosClientException e) {
            log.error("腾讯云COS操作失败 [错误码：{}]：{}", e.getErrorCode(), e.getMessage());
            throw new RuntimeException("云存储服务异常", e);
        } catch (IllegalArgumentException e) {
            log.error("参数校验失败：{}", e.getMessage());
            throw e;
        } catch (IOException e) {
            log.error("文件流操作异常：{}", e.getMessage());
            throw new RuntimeException("文件读取失败", e);
        } finally {
            // 确保关闭COSClient
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }
}