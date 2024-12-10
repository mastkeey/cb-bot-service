package ru.mastkey.telegrambot.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.WorkspaceControllerApi;
import ru.mastkey.model.ChangeWorkspaceNameRequest;
import ru.mastkey.model.CreateWorkspaceRequest;
import ru.mastkey.model.PageWorkspaceResponse;
import ru.mastkey.model.WorkspaceResponse;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.AuthService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceImplTest {

    @Mock
    private WorkspaceControllerApi workspaceControllerApi;

    @Mock
    private AuthService authService;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    @Test
    void createWorkspace_ShouldReturnOk_WhenApiCallSucceeds() throws ApiException {
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));

        HttpStatus result = workspaceService.createWorkspace(1L, "Test Workspace");

        assertThat(result).isEqualTo(HttpStatus.OK);
        verify(workspaceControllerApi).createWorkspace(any(CreateWorkspaceRequest.class), anyMap());
    }

    @Test
    void createWorkspace_ShouldReturnBadRequest_WhenApiThrowsException() throws ApiException {
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));
        doThrow(new ApiException()).when(workspaceControllerApi).createWorkspace(any(CreateWorkspaceRequest.class), anyMap());

        HttpStatus result = workspaceService.createWorkspace(1L, "Test Workspace");

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getWorkspaceList_ShouldReturnKeyboardInfo_WhenApiCallSucceeds() throws ApiException {
        PageWorkspaceResponse response = new PageWorkspaceResponse().totalPages(2).content(List.of());
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));
        when(workspaceControllerApi.getWorkspaces(anyInt(), anyInt(), anyMap())).thenReturn(response);

        KeyboardInfo result = workspaceService.getWorkspaceList(1L, 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.pageTotal()).isEqualTo(2);
        assertThat(result.buttonInfoList()).isEmpty();
    }

    @Test
    void getWorkspaceList_ShouldReturnNull_WhenApiThrowsException() throws ApiException {
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));
        doThrow(new ApiException()).when(workspaceControllerApi).getWorkspaces(anyInt(), anyInt(), anyMap());

        KeyboardInfo result = workspaceService.getWorkspaceList(1L, 0, 10);

        assertThat(result).isNull();
    }

    @Test
    void updateWorkspace_ShouldReturnOk_WhenApiCallSucceeds() throws ApiException {
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));

        HttpStatus result = workspaceService.updateWorkspace(1L, UUID.randomUUID(), "New Workspace Name");

        assertThat(result).isEqualTo(HttpStatus.OK);
        verify(workspaceControllerApi).changeWorkspaceName(any(UUID.class), any(ChangeWorkspaceNameRequest.class), anyMap());
    }

    @Test
    void updateWorkspace_ShouldReturnBadRequest_WhenApiThrowsException() throws ApiException {
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));
        doThrow(new ApiException()).when(workspaceControllerApi).changeWorkspaceName(any(UUID.class), any(ChangeWorkspaceNameRequest.class), anyMap());

        HttpStatus result = workspaceService.updateWorkspace(1L, UUID.randomUUID(), "New Workspace Name");

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void deleteWorkspace_ShouldReturnOk_WhenApiCallSucceeds() throws ApiException {
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));

        HttpStatus result = workspaceService.deleteWorkspace(1L, UUID.randomUUID());

        assertThat(result).isEqualTo(HttpStatus.OK);
        verify(workspaceControllerApi).deleteWorkspace(any(UUID.class), anyMap());
    }

    @Test
    void deleteWorkspace_ShouldReturnBadRequest_WhenApiThrowsException() throws ApiException {
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));
        doThrow(new ApiException()).when(workspaceControllerApi).deleteWorkspace(any(UUID.class), anyMap());

        HttpStatus result = workspaceService.deleteWorkspace(1L, UUID.randomUUID());

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getAllWorkspace_ShouldReturnWorkspaces_WhenApiCallSucceeds() throws ApiException {
        List<WorkspaceResponse> responseList = List.of(new WorkspaceResponse(), new WorkspaceResponse());
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));
        when(workspaceControllerApi.getAllWorkspaces(anyMap())).thenReturn(responseList);

        List<WorkspaceResponse> result = workspaceService.getAllWorkspace(1L);

        assertThat(result).hasSize(2);
    }

    @Test
    void getAllWorkspace_ShouldReturnEmptyList_WhenApiThrowsException() throws ApiException {
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));
        doThrow(new ApiException()).when(workspaceControllerApi).getAllWorkspaces(anyMap());

        List<WorkspaceResponse> result = workspaceService.getAllWorkspace(1L);

        assertThat(result).isEmpty();
    }
}