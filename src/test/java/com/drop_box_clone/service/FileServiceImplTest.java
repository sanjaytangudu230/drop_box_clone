package com.drop_box_clone.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.drop_box_clone.entites.File;
import com.drop_box_clone.exception.DropBoxException;
import com.drop_box_clone.repository.FileRepository;
import com.drop_box_clone.utils.S3Utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

@ContextConfiguration(classes = {FileServiceImpl.class})
@ExtendWith(SpringExtension.class)
class FileServiceImplTest {
    @MockBean
    private FileRepository fileRepository;

    @Autowired
    private FileServiceImpl fileServiceImpl;

    @MockBean
    private S3Utils s3Utils;


    @Test
    void testCreateFile2() throws IOException {
        doNothing().when(s3Utils).uploadFile(Mockito.<String>any(), Mockito.<String>any(), Mockito.<MultipartFile>any());
        when(fileRepository.save(Mockito.<File>any()))
                .thenThrow(new DropBoxException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(DropBoxException.class, () -> fileServiceImpl.createFile("42",
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")))));
        verify(s3Utils).uploadFile(Mockito.<String>any(), Mockito.<String>any(), Mockito.<MultipartFile>any());
        verify(fileRepository).save(Mockito.<File>any());
    }


    @Test
    void testGetFileData() throws IOException {
        assertThrows(DropBoxException.class, () -> fileServiceImpl.getFileData("42", "42"));
    }


    @Test
    void testUpdateFile() throws IOException {
        assertThrows(DropBoxException.class, () -> fileServiceImpl.updateFile("42", "42",
                new MockMultipartFile("Name", new ByteArrayInputStream("AXAXAXAX".getBytes("UTF-8")))));
    }


    @Test
    void testDeleteFile() {
        assertThrows(DropBoxException.class, () -> fileServiceImpl.deleteFile("42", "42"));
    }

    @Test
    void testGetFilesList() throws Exception {
        when(fileRepository.findByUserIdAndIsActive(Mockito.<String>any(), Mockito.<Boolean>any()))
                .thenReturn(new ArrayList<>());
        assertTrue(fileServiceImpl.getFilesList("42").isEmpty());
        verify(fileRepository).findByUserIdAndIsActive(Mockito.<String>any(), Mockito.<Boolean>any());
    }


    @Test
    void testGetFilesList2() throws Exception {
        when(fileRepository.findByUserIdAndIsActive(Mockito.<String>any(), Mockito.<Boolean>any()))
                .thenThrow(new DropBoxException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(DropBoxException.class, () -> fileServiceImpl.getFilesList("42"));
        verify(fileRepository).findByUserIdAndIsActive(Mockito.<String>any(), Mockito.<Boolean>any());
    }

}

