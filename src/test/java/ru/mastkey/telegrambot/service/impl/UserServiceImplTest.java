package ru.mastkey.telegrambot.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.UserControllerApi;
import ru.mastkey.model.CreateUserRequest;
import ru.mastkey.telegrambot.service.AuthService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserControllerApi userControllerApi;

    @Mock
    private AuthService authService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void connectWorkspace_ShouldReturnOk_WhenApiCallSucceeds() throws ApiException {
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));

        doNothing().when(userControllerApi).addNewWorkspace(any(UUID.class), anyMap());

        HttpStatus result = userService.connectWorkspace(1L, UUID.randomUUID());

        assertThat(result).isEqualTo(HttpStatus.OK);
        verify(userControllerApi).addNewWorkspace(any(UUID.class), anyMap());
        verify(authService).getAuthorizationHeader(anyLong());
    }

    @Test
    void connectWorkspace_ShouldReturnBadRequest_WhenApiThrowsException() throws ApiException {
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(Map.of("Authorization", "Bearer token"));

        doThrow(new ApiException()).when(userControllerApi).addNewWorkspace(any(UUID.class), anyMap());

        HttpStatus result = userService.connectWorkspace(1L, UUID.randomUUID());

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userControllerApi).addNewWorkspace(any(UUID.class), anyMap());
        verify(authService).getAuthorizationHeader(anyLong());
    }

    @Test
    void connectWorkspace_ShouldIncludeHeaders_WhenHeadersArePresent() throws ApiException {
        UUID workspaceId = UUID.randomUUID();
        String requestId = "12345";
        MDC.put("MDC_REQUEST_ID", requestId);
        Map<String, String> authHeader = Map.of("Authorization", "Bearer token");
        when(authService.getAuthorizationHeader(anyLong())).thenReturn(authHeader);

        HttpStatus result = userService.connectWorkspace(1L, workspaceId);

        assertThat(result).isEqualTo(HttpStatus.OK);
    }
}