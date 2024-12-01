package org.example.telegrambot.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import org.example.telegrambot.config.properties.UrlProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.mastkey.client.ApiClient;
import ru.mastkey.client.api.FileControllerApi;
import ru.mastkey.client.api.UserControllerApi;
import ru.mastkey.client.api.WorkspaceControllerApi;

@Configuration
public class ControllerConfiguration {

    @Bean
    public ApiClient apiClient(UrlProperties urlProperties) {
        ApiClient apiClient = new ApiClient(httpClient());
        apiClient.setBasePath(urlProperties.cloudServiceUrl);
        return apiClient;
    }

    @Bean
    public CloseableHttpClient httpClient() {
        return HttpClients.createDefault();
    }

    @Bean
    public FileControllerApi fileControllerApi(ApiClient apiClient) {
        return new FileControllerApi(apiClient);
    }

    @Bean
    public UserControllerApi userControllerApi(ApiClient apiClient) {
        return new UserControllerApi(apiClient);
    }

    @Bean
    public WorkspaceControllerApi workspaceControllerApi(ApiClient apiClient) {
        return new WorkspaceControllerApi(apiClient);
    }
}
