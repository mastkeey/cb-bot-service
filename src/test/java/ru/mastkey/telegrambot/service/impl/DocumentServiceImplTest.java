package ru.mastkey.telegrambot.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import com.pengrad.telegrambot.model.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.FileControllerApi;
import ru.mastkey.model.PageFileResponse;
import ru.mastkey.telegrambot.configuration.properties.TokenProperties;
import ru.mastkey.telegrambot.model.FileUploadInfo;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.AuthService;
import ru.mastkey.telegrambot.util.Constants;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {
    @Mock
    private Cache<Long, Set<FileUploadInfo>> fileCache;

    @Mock
    private TokenProperties tokenProperties;

    @Mock
    private FileControllerApi fileControllerApi;

    @Mock
    private Map<Long, UUID> currentWorkspace;

    @Mock
    private AuthService authService;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Test
    void addFile_ShouldAddNewFile_WhenBelowMaxLimit() {
        Set<FileUploadInfo> files = new HashSet<>();
        when(fileCache.get(anyLong(), any())).thenReturn(files);

        List<String> result = documentService.addFile(1L, "file1", mock(File.class));

        assertThat(result).containsExactly("file1");
        assertThat(files).hasSize(1);
    }

    @Test
    void addFile_ShouldNotAddFile_WhenAtMaxLimit() {
        Set<FileUploadInfo> files = new HashSet<>();
        for (int i = 0; i < Constants.DEFAULT_FILE_MAX_COUNT; i++) {
            files.add(new FileUploadInfo("file" + i, mock(File.class)));
        }
        when(fileCache.get(anyLong(), any())).thenReturn(files);

        List<String> result = documentService.addFile(1L, "newFile", mock(File.class));

        assertThat(result).doesNotContain("newFile");
        assertThat(files).hasSize(Constants.DEFAULT_FILE_MAX_COUNT);
    }

    @Test
    void deleteFile_ShouldReturnOk_WhenApiCallSucceeds() throws ApiException {
        HttpStatus result = documentService.deleteFile(1L, UUID.randomUUID(), UUID.randomUUID());

        assertThat(result).isEqualTo(HttpStatus.OK);
        verify(fileControllerApi).deleteFile(any(UUID.class), any(UUID.class), anyMap());
    }

    @Test
    void deleteFile_ShouldReturnBadRequest_WhenApiThrowsException() throws ApiException {
        doThrow(new ApiException()).when(fileControllerApi).deleteFile(any(UUID.class), any(UUID.class), anyMap());

        HttpStatus result = documentService.deleteFile(1L, UUID.randomUUID(), UUID.randomUUID());

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getFileList_ShouldReturnKeyboardInfo_WhenApiCallSucceeds() throws ApiException {
        PageFileResponse response = new PageFileResponse().totalPages(1).content(List.of());
        when(fileControllerApi.getFilesInfo(any(UUID.class), anyInt(), anyInt(), anyMap())).thenReturn(response);

        KeyboardInfo result = documentService.getFileList(1L, UUID.randomUUID(), 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.pageTotal()).isEqualTo(1);
        assertThat(result.buttonInfoList()).isEmpty();
    }

    @Test
    void getFileList_ShouldReturnNull_WhenApiThrowsException() throws ApiException {
        doThrow(new ApiException()).when(fileControllerApi).getFilesInfo(any(UUID.class), anyInt(), anyInt(), anyMap());

        KeyboardInfo result = documentService.getFileList(1L, UUID.randomUUID(), 0, 10);

        assertThat(result).isNull();
    }

    @Test
    void getFile_ShouldReturnMockMultipartFile_WhenApiCallSucceeds() throws ApiException {
        MockMultipartFile mockFile = new MockMultipartFile("file", new byte[0]);
        when(fileControllerApi.getFile(any(UUID.class), any(UUID.class), anyMap())).thenReturn(mockFile);

        MockMultipartFile result = documentService.getFile(1L, UUID.randomUUID(), UUID.randomUUID());

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("file");
    }

    @Test
    void getFile_ShouldReturnNull_WhenApiThrowsException() throws ApiException {
        doThrow(new ApiException()).when(fileControllerApi).getFile(any(UUID.class), any(UUID.class), anyMap());

        MockMultipartFile result = documentService.getFile(1L, UUID.randomUUID(), UUID.randomUUID());

        assertThat(result).isNull();
    }

    @Test
    void uploadFiles_ShouldReturnFalse_WhenNoFilesToUpload() throws ApiException {
        when(fileCache.getIfPresent(anyLong())).thenReturn(null);

        boolean result = documentService.uploadFiles(1L);

        assertThat(result).isFalse();
        verify(fileControllerApi, never()).uploadFiles(any(UUID.class), anyList(), anyMap());
    }

    @Test
    void getFileUrl_ShouldReturnValidUrl() {
        TokenProperties tokenProperties = new TokenProperties();
        tokenProperties.setToken("testToken");
        DocumentServiceImpl documentServiceWithToken = new DocumentServiceImpl(
                fileCache, tokenProperties, fileControllerApi, currentWorkspace, authService
        );

        FileUploadInfo fileUploadInfo = new FileUploadInfo("testFile", mock(File.class));
        when(fileUploadInfo.file().filePath()).thenReturn("path/to/file");

        String url = documentServiceWithToken.getFileUrl(fileUploadInfo);

        assertThat(url).isEqualTo("https://api.telegram.org/file/bottestToken/path/to/file");
    }

    @Test
    void uploadFiles_ShouldReturnFalse_WhenNoFilesPresent() {
        Long userId = 1L;

        when(fileCache.getIfPresent(userId)).thenReturn(null);

        boolean result = documentService.uploadFiles(userId);

        assertThat(result).isFalse();
    }

}