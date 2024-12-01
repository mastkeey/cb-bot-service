package org.example.telegrambot.service.workspace;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, WireMockExtension.class})
@WireMockTest(httpPort = 8080)
public class CreateWorkspaceTest {

    /*String json = """
        {
            "name" : "test",
            "userId" : 1
        }
        """;

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
        when(apiConfig.getWorkspaceUrl()).thenReturn("http://localhost:8080/api/v1/workspaces");
    }

    @Test
    void createWorkspace_ShouldReturnHttpOk() throws JsonProcessingException {
        stubFor(post(urlEqualTo("/api/v1/workspaces"))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)));

        when(objectMapper.writeValueAsString(any())).thenReturn(json);

        HttpStatus result = workspaceServiceImpl.createWorkspace("test", 1L);

        assertThat(result).isEqualTo(HttpStatus.OK);
        verify(1, postRequestedFor(urlEqualTo("/api/v1/workspaces"))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void createWorkspace_ShouldReturnInternalServerError() throws JsonProcessingException {
        stubFor(post(urlEqualTo("/api/v1/workspaces"))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(500)));

        when(objectMapper.writeValueAsString(any())).thenReturn(json);

        HttpStatus result = workspaceServiceImpl.createWorkspace("test", 1L);

        assertThat(result).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(1, postRequestedFor(urlEqualTo("/api/v1/workspaces"))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void createWorkspace_ShouldReturnBadRequest() throws JsonProcessingException {
        stubFor(post(urlEqualTo("/api/v1/workspaces"))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(400)));

        when(objectMapper.writeValueAsString(any())).thenReturn(json);

        HttpStatus result = workspaceServiceImpl.createWorkspace("test", 1L);

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(1, postRequestedFor(urlEqualTo("/api/v1/workspaces"))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void createWorkspace_ShouldReturnBadRequest_WhenHttpClientFails () throws IOException {
        stubFor(post(urlEqualTo("/api/v1/workspaces"))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)));

        when(objectMapper.writeValueAsString(any())).thenReturn(json);
        doThrow(ClientProtocolException.class)
            .when(httpClient)
            .execute(any(HttpPost.class));

        HttpStatus result = workspaceServiceImpl.createWorkspace("test", 1L);

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(0, postRequestedFor(urlEqualTo("/api/v1/workspaces"))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void createWorkspace_ShouldReturnBadRequest_WhenJsonParseFails () throws IOException {
        stubFor(post(urlEqualTo("/api/v1/workspaces"))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)));

        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        HttpStatus result = workspaceServiceImpl.createWorkspace("test", 1L);

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(0, postRequestedFor(urlEqualTo("/api/v1/workspaces"))
            .withHeader("Content-Type", equalTo("application/json")));
    }*/
}
