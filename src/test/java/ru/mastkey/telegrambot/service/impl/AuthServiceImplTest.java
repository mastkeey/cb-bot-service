package ru.mastkey.telegrambot.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.UserControllerApi;
import ru.mastkey.model.AuthUserRequest;
import ru.mastkey.model.TokenResponse;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private Cache<Long, String> tokenCache;

    @Mock
    private UserControllerApi userControllerApi;

    @InjectMocks
    private AuthServiceImpl authService;

    private final Long userId = 123L;

    @BeforeEach
    void setUp() {
        when(tokenCache.get(any(), any())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);
            return ((java.util.function.Function<Long, String>) invocation.getArgument(1)).apply(id);
        });
    }

    @Test
    void authUser_ShouldReturnToken_WhenApiCallSucceeds() throws ApiException {
        TokenResponse response = new TokenResponse().token("valid-token");
        when(userControllerApi.authUser(any(AuthUserRequest.class))).thenReturn(response);

        String token = authService.authUser(userId);

        assertThat(token).isEqualTo("valid-token");
        verify(userControllerApi).authUser(any(AuthUserRequest.class));
        verify(tokenCache).get(eq(userId), any());
    }

    @Test
    void authUser_ShouldReturnEmptyString_WhenApiCallFails() throws ApiException {
        when(userControllerApi.authUser(any(AuthUserRequest.class))).thenThrow(new ApiException("API error"));

        String token = authService.authUser(userId);

        assertThat(token).isEmpty();
        verify(userControllerApi).authUser(any(AuthUserRequest.class));
        verify(tokenCache).get(eq(userId), any());
    }

    @Test
    void getAuthorizationHeader_ShouldReturnAuthorizationHeader() throws ApiException {
        TokenResponse response = new TokenResponse().token("header-token");
        when(userControllerApi.authUser(any(AuthUserRequest.class))).thenReturn(response);

        Map<String, String> headers = authService.getAuthorizationHeader(userId);

        assertThat(headers).containsEntry("Authorization", "Bearer header-token");
        verify(userControllerApi).authUser(any(AuthUserRequest.class));
        verify(tokenCache).get(eq(userId), any());
    }
}