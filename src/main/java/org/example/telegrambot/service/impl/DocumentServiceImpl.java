package org.example.telegrambot.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.pengrad.telegrambot.model.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.telegrambot.config.properties.TokenProperties;
import org.example.telegrambot.model.FileUploadInfo;
import org.example.telegrambot.model.KeyboardInfo;
import org.example.telegrambot.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.FileControllerApi;
import ru.mastkey.model.PageFileResponse;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final Cache<Long, Set<FileUploadInfo>> caffeine;
    private final TokenProperties tokenProperties;
    private final FileControllerApi fileControllerApi;

    public List<String> addFile(Long userId, String fileName, File file) {
        Set<FileUploadInfo> files = caffeine.get(userId, key -> new HashSet<>());
        FileUploadInfo fileUploadInfo = new FileUploadInfo(fileName, file);
        files.remove(fileUploadInfo);
        files.add(fileUploadInfo);
        return files.stream().map(FileUploadInfo::name).toList();
    }

    public Boolean uploadFiles(Long userId) {
        Set<FileUploadInfo> files = caffeine.getIfPresent(userId);
        if (files != null) {
            try {
                sendFiles(downloadFile(files), userId);
                files.clear();
                return true;
            } catch (ApiException e) {
                return false;
            }
        }
        return false;
    }

    private String getFileUrl(FileUploadInfo file) {
        return UriComponentsBuilder.fromHttpUrl("https://api.telegram.org/file/bot")
            .path("{token}/{file}")
            .buildAndExpand(tokenProperties.getToken(), file.file().filePath())
            .toUriString();
    }


    private List<MockMultipartFile> downloadFile(Set<FileUploadInfo> files) {
        return files.stream()
            .map(file -> {
                try {
                    return new MockMultipartFile(file.name(), new URL(getFileUrl(file)).openStream());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).toList();
    }

    private void sendFiles(List<MockMultipartFile> files, Long userId) throws ApiException {
        fileControllerApi.uploadFiles(userId, files);
    }

    public HttpStatus deleteFile(UUID fileId) {
        try {
            fileControllerApi.deleteFile(fileId);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public KeyboardInfo getFileList(Integer pageNumber, Integer pageSize, Long userId) {
        try {
            PageFileResponse filesInfo = fileControllerApi.getFilesInfo(userId, pageNumber, pageSize);
            return new KeyboardInfo(filesInfo.getTotalPages(),
                filesInfo.getContent().stream().map(KeyboardInfo::parseInfo).toList());
        } catch (ApiException e) {
            return null;
        }
    }

    public MockMultipartFile getFile(UUID fileId) {
        try {
            return fileControllerApi.getFile(fileId);
        } catch (ApiException e) {
            return null;
        }
    }
}
