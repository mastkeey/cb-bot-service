package org.example.telegrambot.service.workspace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, WireMockExtension.class})
@WireMockTest(httpPort = 8080)
public class DeleteWorkspaceTest {

    /*private CloseableHttpClient httpClient;

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
    void deleteWorkspace_ShouldReturnHttpOk() {
        UUID uuid = UUID.randomUUID();
        stubFor(delete(urlEqualTo("/api/v1/workspaces/" + uuid))
            .willReturn(aResponse()
                .withStatus(200)));

        HttpStatus result = workspaceServiceImpl.deleteWorkspace(uuid);

        assertThat(result).isEqualTo(HttpStatus.OK);
        WireMock.verify(1, deleteRequestedFor(urlEqualTo("/api/v1/workspaces/" + uuid)));
    }

    @Test
    void deleteWorkspace_ShouldReturnInternalServerError() {
        UUID uuid = UUID.randomUUID();
        stubFor(delete(urlEqualTo("/api/v1/workspaces/" + uuid))
            .willReturn(aResponse()
                .withStatus(500)));

        HttpStatus result = workspaceServiceImpl.deleteWorkspace(uuid);

        assertThat(result).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        WireMock.verify(1, deleteRequestedFor(urlEqualTo("/api/v1/workspaces/" + uuid)));
    }

    @Test
    void deleteWorkspace_ShouldReturnBadRequest() {
        UUID uuid = UUID.randomUUID();
        stubFor(delete(urlEqualTo("/api/v1/workspaces/" + uuid))
            .willReturn(aResponse()
                .withStatus(400)));

        HttpStatus result = workspaceServiceImpl.deleteWorkspace(uuid);

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        WireMock.verify(1, deleteRequestedFor(urlEqualTo("/api/v1/workspaces/" + uuid)));
    }

    @Test
    void deleteWorkspace_ShouldReturnBadRequest_WhenHttpClientFails () throws IOException {
        UUID uuid = UUID.randomUUID();
        stubFor(delete(urlEqualTo("/api/v1/workspaces/" + uuid))
            .willReturn(aResponse()
                .withStatus(200)));

        doThrow(ClientProtocolException.class)
            .when(httpClient)
            .execute(any(HttpDelete.class));

        HttpStatus result = workspaceServiceImpl.deleteWorkspace(uuid);

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        WireMock.verify(0, deleteRequestedFor(urlEqualTo("/api/v1/workspaces/" + uuid)));
    }*/
}
