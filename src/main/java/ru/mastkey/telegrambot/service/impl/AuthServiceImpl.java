package ru.mastkey.telegrambot.service.impl;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.mastkey.client.ApiException;
import ru.mastkey.client.api.UserControllerApi;
import ru.mastkey.model.AuthUserRequest;
import ru.mastkey.telegrambot.service.AuthService;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final Cache<Long, String> tokenCache;
    private final UserControllerApi userControllerApi;

    public String authUser(Long userId) {
        return tokenCache.get(userId, id -> {
            AuthUserRequest authUserRequest = new AuthUserRequest(id.toString(), id.toString());
            try {
                return userControllerApi.authUser(authUserRequest).getToken();
            } catch (ApiException e) {
                return "";
            }});
    }

    public Map<String, String> getAuthorizationHeader(Long userId) {
        return Map.of("Authorization", "Bearer " + authUser(userId));
    }
}
