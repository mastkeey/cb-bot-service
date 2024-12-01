package org.example.telegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.telegrambot.model.KeyboardInfo;
import org.example.telegrambot.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.WorkspaceControllerApi;
import ru.mastkey.model.CreateWorkspaceRequest;
import ru.mastkey.model.PageWorkspaceResponse;
import ru.mastkey.model.WorkspaceResponse;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceControllerApi workspaceControllerApi;

    public HttpStatus createWorkspace(String name, Long userId) {
        try {
            CreateWorkspaceRequest request = new CreateWorkspaceRequest(name, userId);
            workspaceControllerApi.createWorkspace(request);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public KeyboardInfo getWorkspaceList(Integer pageNumber, Integer pageSize, Long userId) {
        try {
            PageWorkspaceResponse workspaces = workspaceControllerApi.getWorkspaces(userId, pageNumber, pageSize);
            return new KeyboardInfo(workspaces.getTotalPages(),
                workspaces.getContent().stream().map(KeyboardInfo::parseInfo).toList());
        } catch (ApiException e) {
            return null;
        }
    }

    public HttpStatus updateWorkspace(UUID workspaceId, String newWorkspaceName) {
        try {
            workspaceControllerApi.changeWorkspaceName(workspaceId, newWorkspaceName);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus deleteWorkspace(UUID workspaceId) {
        try {
            workspaceControllerApi.deleteWorkspace(workspaceId);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }


}
