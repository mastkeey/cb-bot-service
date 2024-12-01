package org.example.telegrambot.service.workspace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, WireMockExtension.class})
@WireMockTest(httpPort = 8080)
public class GetWorkspaceListTest {

    /*private String response = """
        [
            {
                "workspaceId" : "6afe1644-536d-4c9d-a9cb-83d22b51a068",
                "name" : "test",
                "telegramUserId" : 1
            }
        ]
        """;

    private Long userId = 1L;

    private Integer pageNumber = 1;

    private Integer pageSize = 1;

    private CloseableHttpClient httpClient;

    private WorkspaceServiceImpl workspaceServiceImpl;

    private ObjectMapper objectMapper;

    @Mock
    private ApiConfig apiConfig;


    @BeforeEach
    void setUp() {
        httpClient = spy(HttpClients.createDefault());
        objectMapper = spy(new ObjectMapper());
        workspaceServiceImpl = new WorkspaceServiceImpl(apiConfig, httpClient, objectMapper);
        when(apiConfig.getWorkspaceUrl()).thenReturn("http://localhost:8080/api/v1/workspaces");
    }

    @Test
    void getWorkspaceList_ShouldReturnWorkspaceList_WhenHttpOk() {
        stubFor(get(urlEqualTo("/api/v1/workspaces/users/" + userId + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize))
            .willReturn(aResponse()
                .withHeader("Total-Pages", "1")
                .withBody(response)
                .withStatus(200)));

        KeyboardInfo workspaceList = workspaceServiceImpl.getWorkspaceList(pageNumber, pageSize, userId);

        assertThat(workspaceList.pageTotal()).isEqualTo(1);
        assertThat(workspaceList.buttonInfoList().size()).isEqualTo(1);
        assertThat(workspaceList.buttonInfoList().get(0).name()).isEqualTo("test");
        assertThat(workspaceList.buttonInfoList().get(0).id()).isEqualTo(UUID.fromString("6afe1644-536d-4c9d-a9cb-83d22b51a068"));
        verify(1, getRequestedFor(urlEqualTo("/api/v1/workspaces/users/" + userId + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize)));
    }

    @Test
    void getWorkspaceList_ShouldReturnNull_WhenInternalServerError() {
        stubFor(get(urlEqualTo("/api/v1/workspaces/users/" + userId + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize))
            .willReturn(aResponse()
                .withStatus(500)));

        KeyboardInfo workspaceList = workspaceServiceImpl.getWorkspaceList(pageNumber, pageSize, userId);

        assertThat(workspaceList).isNull();
        verify(1, getRequestedFor(urlEqualTo("/api/v1/workspaces/users/" + userId + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize)));
    }

    @Test
    void getWorkspaceList_ShouldReturnNull_WhenBadRequest() {
        stubFor(get(urlEqualTo("/api/v1/workspaces/users/" + userId + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize))
            .willReturn(aResponse()
                .withStatus(400)));

        KeyboardInfo workspaceList = workspaceServiceImpl.getWorkspaceList(pageNumber, pageSize, userId);

        assertThat(workspaceList).isNull();
        verify(1, getRequestedFor(urlEqualTo("/api/v1/workspaces/users/" + userId + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize)));
    }

    @Test
    void getWorkspaceList_ShouldReturnBadRequest_WhenHttpClientFails () throws IOException {
        stubFor(get(urlEqualTo("/api/v1/workspaces/users/" + userId + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize))
            .willReturn(aResponse()
                .withHeader("Total-Pages", "1")
                .withBody(response)
                .withStatus(200)));

        doThrow(ClientProtocolException.class)
            .when(httpClient)
            .execute(any(HttpGet.class));

        KeyboardInfo workspaceList = workspaceServiceImpl.getWorkspaceList(pageNumber, pageSize, userId);

        assertThat(workspaceList).isNull();
        verify(0, getRequestedFor(urlEqualTo("/api/v1/workspaces/users/" + userId + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize)));
    }

    @Test
    void getWorkspaceList_ShouldReturnBadRequest_WhenJsonParseFails () throws IOException {
        stubFor(get(urlEqualTo("/api/v1/workspaces/users/" + userId + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize))
            .willReturn(aResponse()
                .withHeader("Total-Pages", "1")
                .withBody(response)
                .withStatus(200)));

        doThrow(MismatchedInputException.class)
            .when(objectMapper)
            .readValue(any(InputStream.class), any(CollectionType.class));

        KeyboardInfo workspaceList = workspaceServiceImpl.getWorkspaceList(pageNumber, pageSize, userId);

        assertThat(workspaceList).isNull();
        verify(1, getRequestedFor(urlEqualTo("/api/v1/workspaces/users/" + userId + "?pageNumber=" + pageNumber + "&pageSize=" + pageSize)));
    }*/
}
