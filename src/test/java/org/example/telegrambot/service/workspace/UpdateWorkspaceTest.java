package org.example.telegrambot.service.workspace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPatch;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, WireMockExtension.class})
@WireMockTest(httpPort = 8080)
public class UpdateWorkspaceTest {

    /*private UUID uuid = UUID.randomUUID();

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
        when(apiConfig.getWorkspaceUrl()).thenReturn("http://localhost:8080/api/v1/workspaces");
    }

    @Test
    void updateWorkspace_ShouldReturnHttpOk() {
        stubFor(patch(urlEqualTo("/api/v1/workspaces/" + uuid + "?newWorkspaceName=" + workspaceName))
            .willReturn(aResponse()
                .withStatus(200)));

        HttpStatus result = workspaceServiceImpl.updateWorkspace(uuid, workspaceName);

        assertThat(result).isEqualTo(HttpStatus.OK);
        WireMock.verify(1, patchRequestedFor(
            urlEqualTo("/api/v1/workspaces/" + uuid + "?newWorkspaceName=" + workspaceName)));
    }

    @Test
    void updateWorkspace_ShouldReturnInternalServerError() {
        stubFor(patch(urlEqualTo("/api/v1/workspaces/" + uuid + "?newWorkspaceName=" + workspaceName))
            .willReturn(aResponse()
                .withStatus(500)));

        HttpStatus result = workspaceServiceImpl.updateWorkspace(uuid, workspaceName);

        assertThat(result).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        WireMock.verify(1, patchRequestedFor(urlEqualTo("/api/v1/workspaces/" + uuid + "?newWorkspaceName=" + workspaceName)));
    }

    @Test
    void updateWorkspace_ShouldReturnBadRequest() {
        stubFor(patch(urlEqualTo("/api/v1/workspaces/" + uuid + "?newWorkspaceName=" + workspaceName))
            .willReturn(aResponse()
                .withStatus(400)));

        HttpStatus result = workspaceServiceImpl.updateWorkspace(uuid, workspaceName);

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        WireMock.verify(1, patchRequestedFor(urlEqualTo("/api/v1/workspaces/" + uuid + "?newWorkspaceName=" + workspaceName)));
    }

    @Test
    void updateWorkspace_ShouldReturnBadRequest_WhenHttpClientFails () throws IOException {
        stubFor(patch(urlEqualTo("/api/v1/workspaces/" + uuid + "?newWorkspaceName=" + workspaceName))
            .withQueryParam("newWorkspaceName", equalTo(workspaceName))
            .willReturn(aResponse()
                .withStatus(200)));

        doThrow(ClientProtocolException.class)
            .when(httpClient)
            .execute(any(HttpPatch.class));

        HttpStatus result = workspaceServiceImpl.updateWorkspace(uuid, workspaceName);

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        WireMock.verify(0, patchRequestedFor(urlEqualTo("/api/v1/workspaces/" + uuid + "?newWorkspaceName=" + workspaceName)));
    }*/
}
