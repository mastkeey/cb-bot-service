package org.example.telegrambot.service;

import com.pengrad.telegrambot.model.File;
import org.example.telegrambot.model.KeyboardInfo;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    List<String> addFile(Long userId, String fileName, File file);
    Boolean uploadFiles(Long userId);
    HttpStatus deleteFile(UUID fileId);
    MockMultipartFile getFile(UUID fileId);
    KeyboardInfo getFileList(Integer pageNumber, Integer pageSize, Long userId);
}
