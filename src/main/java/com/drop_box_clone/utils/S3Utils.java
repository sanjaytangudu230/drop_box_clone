package com.drop_box_clone.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@Component
public class S3Utils {

    private final AmazonS3 s3Client;

    @Autowired
    public S3Utils(AmazonS3 s3Client) {
       this.s3Client = s3Client;
    }

    public void uploadFile (String bucket, String filePath, MultipartFile file) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        PutObjectResult putObjectResult = s3Client.putObject(bucket, filePath, file.getInputStream(), objectMetadata);
    }

    public byte[] getFile (String bucket, String filePath) throws IOException {
        S3Object object = s3Client.getObject(new GetObjectRequest(bucket, filePath));
        InputStream objectData = object.getObjectContent();
        return IOUtils.toByteArray(objectData);
    }


}
