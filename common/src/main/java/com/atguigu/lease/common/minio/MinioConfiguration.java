package com.atguigu.lease.common.minio;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan("com.atguigu.lease.common.minio")
public class MinioConfiguration {

    @Autowired  
    private MinioProperties minio;

   @Bean
    public MinioClient createMinioClient() {
        return createMinioClient().builder().endpoint(minio.getEndpoint()).credentials(minio.getAccessKey(), minio.getSecretKey()).build();
    }
}
