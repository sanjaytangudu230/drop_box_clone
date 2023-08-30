package com.drop_box_clone.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${s3.access.key}")
    String accessKey;
    @Value("${s3.secret.key}")
    String accessSecretKey;

    @Bean
    public AmazonS3 amazonS3 () {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecretKey);
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:9000", Regions.DEFAULT_REGION.getName()))  // Use the default region
                .withPathStyleAccessEnabled(true)  // Enable path-style access for MinIO
                .build();
    }
}
