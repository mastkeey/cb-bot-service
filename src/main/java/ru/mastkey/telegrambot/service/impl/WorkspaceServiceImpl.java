package ru.mastkey.telegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.WorkspaceControllerApi;
import ru.mastkey.model.ChangeWorkspaceNameRequest;
import ru.mastkey.model.CreateWorkspaceRequest;
import ru.mastkey.model.PageWorkspaceResponse;
import ru.mastkey.model.WorkspaceResponse;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.AuthService;
import ru.mastkey.telegrambot.service.WorkspaceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.mastkey.telegrambot.util.Constants.HEADER_REQUEST_ID;
import static ru.mastkey.telegrambot.util.Constants.MDC_REQUEST_ID;

@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceControllerApi workspaceControllerApi;
    private final AuthService authService;

    public HttpStatus createWorkspace(Long userId, String name) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID));
            headers.putAll(authService.getAuthorizationHeader(userId));
            CreateWorkspaceRequest request = new CreateWorkspaceRequest(name);
            workspaceControllerApi.createWorkspace(request, headers);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public KeyboardInfo getWorkspaceList(Long userId, Integer pageNumber, Integer pageSize) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID));
            headers.putAll(authService.getAuthorizationHeader(userId));
            PageWorkspaceResponse workspaces = workspaceControllerApi.getWorkspaces(pageNumber, pageSize, headers);
            return new KeyboardInfo(workspaces.getTotalPages(),
                workspaces.getContent().stream().map(KeyboardInfo::parseInfo).toList());
        } catch (ApiException e) {
            return null;
        }
    }

    public HttpStatus updateWorkspace(Long userId, UUID workspaceId, String newWorkspaceName) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID));
            headers.putAll(authService.getAuthorizationHeader(userId));
            workspaceControllerApi.changeWorkspaceName(workspaceId, new ChangeWorkspaceNameRequest(newWorkspaceName), headers);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus deleteWorkspace(Long userId, UUID workspaceId) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID));
            headers.putAll(authService.getAuthorizationHeader(userId));
            workspaceControllerApi.deleteWorkspace(workspaceId, headers);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public List<WorkspaceResponse> getAllWorkspace(Long userId) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID));
            headers.putAll(authService.getAuthorizationHeader(userId));
            return workspaceControllerApi.getAllWorkspaces(headers);
        } catch (ApiException e) {
            return List.of();
        }
    }
}
