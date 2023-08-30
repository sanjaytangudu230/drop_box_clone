package com.drop_box_clone.service;

import com.drop_box_clone.dto.pojos.MetaDataPojo;
import com.drop_box_clone.dto.responses.CreateFileResponse;
import com.drop_box_clone.dto.responses.FileMetadataResponse;
import com.drop_box_clone.entites.File;
import com.drop_box_clone.exception.DropBoxException;
import com.drop_box_clone.repository.FileRepository;
import com.drop_box_clone.utils.JsonUtils;
import com.drop_box_clone.utils.S3Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService{

    private final S3Utils s3Utils;
    private final FileRepository fileRepository;


    @Autowired
    public FileServiceImpl (S3Utils s3Utils, FileRepository fileRepository) {
        this.s3Utils = s3Utils;
        this.fileRepository = fileRepository;
    }

    @Value("${s3.bucket}")
    private String s3BucketName;

    @Value("${s3.folder}")
    private String s3FolderName;


    @Override
    public CreateFileResponse createFile (String userId, MultipartFile inputFile) {
        File file = new File();
        MetaDataPojo metaDataPojo = getMetaData(inputFile);
        String metadataJson = JsonUtils.getJson(metaDataPojo);
        file.setMetadata(metadataJson);
        file.setUserId(userId);
        file.setIsActive(Boolean.TRUE);
        String s3FileName = getRandomUUID().toString();
        try {
            s3Utils.uploadFile(s3BucketName, s3FolderName + s3FileName, inputFile);
        } catch (Exception e) {
            log.info("Unable to upload file{}", e.getMessage());
            throw new DropBoxException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        file.setS3FileName(s3FileName);
        fileRepository.save(file);
        CreateFileResponse response = new CreateFileResponse();
        response.setFileId(file.getId().toString());
        return response;
    }

    private MetaDataPojo getMetaData (MultipartFile file) {
        MetaDataPojo metaDataPojo = new MetaDataPojo();
        metaDataPojo.setFileName(file.getName());
        metaDataPojo.setFileType(file.getContentType());
        metaDataPojo.setFileSize(file.getSize());
        return metaDataPojo;
    }

    private UUID getRandomUUID () {
        return UUID.randomUUID();
    }

    public byte[] getFileData(String userId, String fileId) throws IOException {
        File file = getFile(fileId);
        if (Objects.isNull(file)) {
            throw new DropBoxException(HttpStatus.BAD_REQUEST, "Invalid fileId");
        } else if (!Objects.equals(file.getUserId(), userId)) {
            throw new DropBoxException(HttpStatus.UNAUTHORIZED, "User not authorized to access this file");
        }
        return s3Utils.getFile(s3BucketName, s3FolderName + file.getS3FileName());
    }

    private File getFile(String fileId) {
        UUID fileUUID;
        try {
            fileUUID = UUID.fromString(fileId);
        } catch (IllegalArgumentException ex) {
            throw new DropBoxException(HttpStatus.BAD_REQUEST, "Invalid fileId");
        }
        return fileRepository.findByIdAndIsActive(fileUUID, Boolean.TRUE);
    }

    @Override
    public void updateFile (String userId, String fileId, MultipartFile inputFile){
        File file = getFile(fileId);
        if (Objects.isNull(file)) {
            throw new DropBoxException(HttpStatus.BAD_REQUEST, "Invalid fileId");
        } else if (!Objects.equals(file.getUserId(), userId)) {
            throw new DropBoxException(HttpStatus.UNAUTHORIZED, "User not authorized to access this file");
        }

        MetaDataPojo metaDataPojo = getMetaData(inputFile);
        String metadataJson = JsonUtils.getJson(metaDataPojo);
        file.setMetadata(metadataJson);
        try {
            s3Utils.uploadFile(s3BucketName, s3FolderName + file.getS3FileName(), inputFile);
        } catch (IOException exception) {
            throw new DropBoxException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        }
        fileRepository.save(file);
    }

    @Override
    public void deleteFile (String userId, String fileId) {
        File file = getFile(fileId);
        if (Objects.isNull(file)) {
            throw new DropBoxException(HttpStatus.BAD_REQUEST, "Invalid fileId");
        } else if (!Objects.equals(file.getUserId(), userId)) {
            throw new DropBoxException(HttpStatus.UNAUTHORIZED, "User not authorized to access this file");
        }
        file.setIsActive(Boolean.FALSE);
        fileRepository.save(file);
    }

    @Override
    public List<FileMetadataResponse> getFilesList (String userId) throws Exception {
        List<File> filesList = fileRepository.findByUserIdAndIsActive(userId, Boolean.TRUE);
        List<FileMetadataResponse> fileMetadataResponses = new ArrayList<>();
        for (File file : filesList) {
            String metaDataJson = file.getMetadata();
            MetaDataPojo metaDataPojo = JsonUtils.convertToPojo(metaDataJson, MetaDataPojo.class);
            FileMetadataResponse fileMetadataResponse = new FileMetadataResponse();
            fileMetadataResponse.setFileName(metaDataPojo.getFileName());
            fileMetadataResponse.setFileId(file.getId().toString());
            fileMetadataResponse.setCreatedAt(String.valueOf(file.getCreatedAt()));
            fileMetadataResponse.setFileType(metaDataPojo.getFileType());
            fileMetadataResponses.add(fileMetadataResponse);
        }
        return fileMetadataResponses;
    }
}
