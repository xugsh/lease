package com.atguigu.lease.web.admin.service.impl;

import com.atguigu.lease.common.minio.MinioProperties;
import com.atguigu.lease.web.admin.service.FileService;
import io.minio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private MinioProperties minioProperties;

    @Override
    public String upLoad(MultipartFile file) {

        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().
                            bucket(minioProperties.getBucketName()).
                            build());
            if (!exists) {
                //创建桶
                minioClient.makeBucket(
                        MakeBucketArgs.builder().
                                bucket(minioProperties.getBucketName()).
                                build());
                minioClient.setBucketPolicy(
                        SetBucketPolicyArgs.builder().
                                bucket(minioProperties.getBucketName()).
                                config(createBucketPolicyConfig(minioProperties.getBucketName())).
                                build());
            }
            String filename = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
            minioClient.putObject(PutObjectArgs.builder().
                    bucket(minioProperties.getBucketName()).
                    contentType(file.getContentType()).
                    stream(file.getInputStream(),file.getSize(),-1).
                    build());
            return String.join("/",minioProperties.getEndpoint(),minioProperties.getBucketName(),filename);
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }
    //minio图片访问权限设置
    private String createBucketPolicyConfig(String bucketName) {
        return """
                  {
                    "Statement" : [ {
                      "Action" : "s3:GetObject",
                      "Effect" : "Allow",
                      "Principal" : "*",
                      "Resource" : "arn:aws:s3:::%s/*"
                    } ],
                    "Version" : "2012-10-17"
                  }
                  """.formatted(bucketName);
    }
}
