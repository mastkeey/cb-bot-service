package ru.mastkey.telegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.UserControllerApi;
import ru.mastkey.model.CreateUserRequest;
import ru.mastkey.telegrambot.service.AuthService;
import ru.mastkey.telegrambot.service.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ru.mastkey.telegrambot.util.Constants.HEADER_REQUEST_ID;
import static ru.mastkey.telegrambot.util.Constants.MDC_REQUEST_ID;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserControllerApi userControllerApi;
    private final AuthService authService;

    public HttpStatus createUser(Long userId) {
        try {
            CreateUserRequest request = new CreateUserRequest(userId.toString(), userId.toString());
            userControllerApi.createUser(request, Map.of(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID)));
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

    public HttpStatus connectWorkspace(Long userId, UUID workspaceId) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put(HEADER_REQUEST_ID, MDC.get(MDC_REQUEST_ID));
            headers.putAll(authService.getAuthorizationHeader(userId));
            userControllerApi.addNewWorkspace(workspaceId, headers);
            return HttpStatus.OK;
        } catch (ApiException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }
}
