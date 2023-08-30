package com.drop_box_clone.service;

import com.drop_box_clone.dto.responses.CreateFileResponse;
import com.drop_box_clone.dto.responses.FileMetadataResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileService {
    CreateFileResponse createFile (String userId, MultipartFile inputFile);
    byte[] getFileData (String userId, String fileId) throws IOException;
    void updateFile (String userId, String fileId, MultipartFile inputFile);
    void deleteFile (String userId, String fileId);
    List<FileMetadataResponse> getFilesList (String userId) throws Exception;
}
