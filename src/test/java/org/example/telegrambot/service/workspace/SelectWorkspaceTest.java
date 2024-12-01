package org.example.telegrambot.service.workspace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, WireMockExtension.class})
@WireMockTest(httpPort = 8080)
public class SelectWorkspaceTest {

   /* private Long userId = 1L;

    private String workspaceName = "test";

    private CloseableHttpClient httpClient;

    private WorkspaceServiceImpl workspaceServiceImpl;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ApiConfig apiConfig;

    @BeforeEach
    void setUp() {
        httpClient = spy(HttpClients.createDefault());
        workspaceServiceImpl = new WorkspaceServiceImpl(apiConfig, httpClient, objectMapper);
        when(apiConfig.getUserUrl()).thenReturn("http://localhost:8080/api/v1/users");
    }

    @Test
    void selectWorkspace_ShouldReturnHttpOk() {
        stubFor(post(urlEqualTo("/api/v1/users/" + userId + "/changeCurrentWorkspace?newWorkspaceName=" + workspaceName))
            .willReturn(aResponse()
                .withStatus(200)));

        HttpStatus result = workspaceServiceImpl.selectWorkspace(userId, workspaceName);

        assertThat(result).isEqualTo(HttpStatus.OK);
        WireMock.verify(1, postRequestedFor(urlEqualTo("/api/v1/users/" + userId + "/changeCurrentWorkspace?newWorkspaceName=" + workspaceName)));
    }

    @Test
    void selectWorkspace_ShouldReturnInternalServerError() {
        stubFor(post(urlEqualTo("/api/v1/users/" + userId + "/changeCurrentWorkspace?newWorkspaceName=" + workspaceName))
            .willReturn(aResponse()
                .withStatus(500)));

        HttpStatus result = workspaceServiceImpl.selectWorkspace(userId, workspaceName);

        assertThat(result).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        WireMock.verify(1, postRequestedFor(urlEqualTo("/api/v1/users/" + userId + "/changeCurrentWorkspace?newWorkspaceName=" + workspaceName)));
    }

    @Test
    void selectWorkspace_ShouldReturnBadRequest() {
        stubFor(post(urlEqualTo("/api/v1/users/" + userId + "/changeCurrentWorkspace?newWorkspaceName=" + workspaceName))
            .willReturn(aResponse()
                .withStatus(400)));

        HttpStatus result = workspaceServiceImpl.selectWorkspace(userId, workspaceName);

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        WireMock.verify(1, postRequestedFor(urlEqualTo("/api/v1/users/" + userId + "/changeCurrentWorkspace?newWorkspaceName=" + workspaceName)));
    }

    @Test
    void selectWorkspace_ShouldReturnBadRequest_WhenHttpClientFails () throws IOException {
        stubFor(post(urlEqualTo("/api/v1/users/" + userId + "/changeCurrentWorkspace?newWorkspaceName=" + workspaceName))
            .willReturn(aResponse()
                .withStatus(200)));

        doThrow(ClientProtocolException.class)
            .when(httpClient)
            .execute(any(HttpPost.class));

        HttpStatus result = workspaceServiceImpl.selectWorkspace(userId, workspaceName);

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        WireMock.verify(0, patchRequestedFor(urlEqualTo("/api/v1/users/" + userId + "/changeCurrentWorkspace?newWorkspaceName=" + workspaceName)));
    }*/
}
