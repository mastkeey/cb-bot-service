package org.example.telegrambot.service;

import org.example.telegrambot.model.KeyboardInfo;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public interface WorkspaceService {
    HttpStatus createWorkspace(String name, Long userId);
    KeyboardInfo getWorkspaceList(Integer pageNumber, Integer pageSize, Long userId);
    HttpStatus updateWorkspace(UUID workspaceId, String newWorkspaceName);
    HttpStatus deleteWorkspace(UUID workspaceId);
}
