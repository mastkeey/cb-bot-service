package org.example.telegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.telegrambot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.UserControllerApi;
import ru.mastkey.model.CreateUserRequest;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserControllerApi userControllerApi;

    public HttpStatus createUser(Long userId, Long chatId, String username) {
        try {
            CreateUserRequest request = new CreateUserRequest(username, userId, chatId);
            userControllerApi.createUser(request);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus selectWorkspace(Long userId, UUID workspaceId) {
        try {
            userControllerApi.changeCurrentWorkspace(workspaceId, userId);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
