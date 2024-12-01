package ru.mastkey.telegrambot.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.pengrad.telegrambot.model.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.FileControllerApi;
import ru.mastkey.model.PageFileResponse;
import ru.mastkey.telegrambot.configuration.properties.TokenProperties;
import ru.mastkey.telegrambot.model.FileUploadInfo;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.AuthService;
import ru.mastkey.telegrambot.service.DocumentService;
import ru.mastkey.telegrambot.util.Constants;

import java.net.URL;
import java.util.*;

import static ru.mastkey.telegrambot.util.Constants.HEADER_REQUEST_ID;
import static ru.mastkey.telegrambot.util.Constants.MDC_REQUEST_ID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final Cache<Long, Set<FileUploadInfo>> fileCache;
    private final TokenProperties tokenProperties;
    private final FileControllerApi fileControllerApi;
    private final Map<Long, UUID> currentWorkspace;
    private final AuthService authService;

    public List<String> addFile(Long userId, String fileName, File file) {
        Set<FileUploadInfo> files = fileCache.get(userId, key -> new HashSet<>());
        FileUploadInfo fileUploadInfo = new FileUploadInfo(fileName, file);
        files.remove(fileUploadInfo);
        if (files.size() == Constants.DEFAULT_FILE_MAX_COUNT) {
            return files.stream().map(FileUploadInfo::name).toList();
        }
        files.add(fileUploadInfo);
        return files.stream().map(FileUploadInfo::name).toList();
    }

    public Boolean uploadFiles(Long userId) {
        Set<FileUploadInfo> files = fileCache.getIfPresent(userId);

        if (files != null) {
            try {
                sendFiles(userId, downloadFile(files), currentWorkspace.get(userId));
                files.clear();
                return true;
            } catch (ApiException e) {
                return false;
            }
        }
        return false;
    }

    public String getFileUrl(FileUploadInfo file) {
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

    public void sendFiles(Long userId, List<MockMultipartFile> files, UUID workspaceId) throws ApiException {
        Map<String, String> headers = new HashMap<>();
        headers.put(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID));
        headers.putAll(authService.getAuthorizationHeader(userId));
        fileControllerApi.uploadFiles(workspaceId, files, headers);
    }

    public HttpStatus deleteFile(Long userId, UUID workspaceId, UUID fileId) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID));
            headers.putAll(authService.getAuthorizationHeader(userId));
            fileControllerApi.deleteFile(fileId, workspaceId, headers);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public KeyboardInfo getFileList(Long userId, UUID workspaceId, Integer pageNumber, Integer pageSize) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID));
            headers.putAll(authService.getAuthorizationHeader(userId));
            PageFileResponse filesInfo = fileControllerApi.getFilesInfo(workspaceId, pageNumber, pageSize, headers);
            return new KeyboardInfo(filesInfo.getTotalPages(),
                filesInfo.getContent().stream().map(KeyboardInfo::parseInfo).toList());
        } catch (ApiException e) {
            return null;
        }
    }

    public MockMultipartFile getFile(Long userId, UUID workspaceId, UUID fileId) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID));
            headers.putAll(authService.getAuthorizationHeader(userId));
            return fileControllerApi.getFile(fileId, workspaceId, headers);
        } catch (ApiException e) {
            return null;
        }
    }
}
