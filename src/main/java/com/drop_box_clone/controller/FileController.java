package com.drop_box_clone.controller;

import com.drop_box_clone.constants.APIMappings;
import com.drop_box_clone.dto.pojos.AuthenticatedInfo;
import com.drop_box_clone.dto.responses.BaseApiResponse;
import com.drop_box_clone.dto.responses.CreateFileResponse;
import com.drop_box_clone.dto.responses.FileMetadataResponse;
import com.drop_box_clone.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(APIMappings.BASE_URL + APIMappings.FileEndpoints.FILES)
@Slf4j
public class FileController {

    @Resource(name = "requestScopedBean")
    private AuthenticatedInfo authenticatedInfo;

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping(path=APIMappings.FileEndpoints.HEALTH_CHECK)
    public ResponseEntity<?>healthCheck() {
        return new ResponseEntity<>(BaseApiResponse.okResponse("Service is UP"), HttpStatus.OK);
    }

    @GetMapping(path=APIMappings.FileEndpoints.FILE_ID)
    public ResponseEntity<?> getFileById(@PathVariable("fileId") String fileId) throws IOException {
        byte[] fileData = fileService.getFileData(authenticatedInfo.getUserId(), fileId);
        return new ResponseEntity<>(BaseApiResponse.okResponse(fileData), HttpStatus.OK);
    }

    @PostMapping(path = APIMappings.FileEndpoints.UPLOAD)
    public ResponseEntity<?> uploadFile (@RequestBody MultipartFile inputFile) {
        CreateFileResponse fileResponse = fileService.createFile(authenticatedInfo.getUserId(), inputFile);
        return new ResponseEntity<>(BaseApiResponse.okResponse(fileResponse), HttpStatus.OK);
    }

    @PutMapping(path = APIMappings.FileEndpoints.FILE_ID)
    public ResponseEntity<?> updateFile (@PathVariable("fileId") String fileId, @RequestBody MultipartFile inputFile) {
        fileService.updateFile(authenticatedInfo.getUserId(), fileId, inputFile);
        return new ResponseEntity<>(BaseApiResponse.okResponse("File Updated Successfully"), HttpStatus.OK);
    }

    @DeleteMapping(path = APIMappings.FileEndpoints.FILE_ID)
    public ResponseEntity<?> deleteFile (@PathVariable("fileId") String fileId) {
        fileService.deleteFile(authenticatedInfo.getUserId(), fileId);
        return new ResponseEntity<>(BaseApiResponse.okResponse("File Deleted Successfully"), HttpStatus.OK);
    }

    @GetMapping(path = "/")
    public ResponseEntity<?> getFilesList () throws Exception {
        List<FileMetadataResponse> fileMetadataResponseList = fileService.getFilesList(authenticatedInfo.getUserId());
        return new ResponseEntity<>(BaseApiResponse.okResponse(fileMetadataResponseList), HttpStatus.OK);
    }
}
