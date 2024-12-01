package ru.mastkey.telegrambot.service;

import ru.mastkey.telegrambot.model.KeyboardInfo;
import org.springframework.http.HttpStatus;
import ru.mastkey.model.WorkspaceResponse;

import java.util.List;
import java.util.UUID;

public interface WorkspaceService {
    HttpStatus createWorkspace(Long userId, String name);
    KeyboardInfo getWorkspaceList(Long userId, Integer pageNumber, Integer pageSize);
    HttpStatus updateWorkspace(Long userId, UUID workspaceId, String newWorkspaceName);
    HttpStatus deleteWorkspace(Long userId, UUID workspaceId);
    List<WorkspaceResponse> getAllWorkspace(Long userId);
}
