package ru.mastkey.telegrambot.service;

import com.pengrad.telegrambot.model.File;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.UUID;

public interface DocumentService {
    List<String> addFile(Long userId, String fileName, File file);
    Boolean uploadFiles(Long userId);
    HttpStatus deleteFile(Long userId, UUID workspaceId, UUID fileId);
    MockMultipartFile getFile(Long userId, UUID workspaceId, UUID fileId);
    KeyboardInfo getFileList(Long userId, UUID workspaceId, Integer pageNumber, Integer pageSize);
}
