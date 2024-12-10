package ru.mastkey.telegrambot.service;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public interface UserService {
    HttpStatus createUser(Long userId);
    HttpStatus connectWorkspace(Long userId, UUID workspaceId);
}
