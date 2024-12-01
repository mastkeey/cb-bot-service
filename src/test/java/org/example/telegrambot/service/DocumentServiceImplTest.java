package org.example.telegrambot.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith({MockitoExtension.class, WireMockExtension.class})
public class DocumentServiceImplTest {

    @Test
    void sendFile_ShouldReturnHttpOk() {
        stubFor(post(urlEqualTo("/api/v1/files"))
            .withHeader("Content-Type", equalTo("multipart/form-data"))
            .willReturn(aResponse()
                .withStatus(200)));
    }

    @Test
    void sendFile_ShouldReturnInternalServerError() {
        stubFor(post(urlEqualTo("/api/v1/files"))
            .withHeader("Content-Type", equalTo("multipart/form-data"))
            .willReturn(aResponse()
                .withStatus(500)));
    }

    @Test
    void sendFile_ShouldReturnBadRequest() {
        stubFor(post(urlEqualTo("/api/v1/files"))
            .withHeader("Content-Type", equalTo("multipart/form-data"))
            .willReturn(aResponse()
                .withStatus(400)));
    }

    @Test
    void sendFile_ShouldReturnBadRequest_WhenHttpClientFails () {
        stubFor(post(urlEqualTo("/api/v1/files"))
            .withHeader("Content-Type", equalTo("multipart/form-data"))
            .willReturn(aResponse()
                .withStatus(200)));
    }

    @Test
    void deleteFile_ShouldReturnHttpOk() {
        stubFor(post(urlEqualTo("/api/v1/files/" + any(UUID.class)))
            .willReturn(aResponse()
                .withStatus(200)));
    }

    @Test
    void deleteFile_ShouldReturnInternalServerError() {
        stubFor(post(urlEqualTo("/api/v1/files/" + any(UUID.class)))
            .willReturn(aResponse()
                .withStatus(500)));
    }

    @Test
    void deleteFile_ShouldReturnBadRequest() {
        stubFor(post(urlEqualTo("/api/v1/files/" + any(UUID.class)))
            .willReturn(aResponse()
                .withStatus(400)));
    }

    @Test
    void deleteFile_ShouldReturnBadRequest_WhenHttpClientFails () {
        stubFor(post(urlEqualTo("/api/v1/files/" + any(UUID.class)))
            .willReturn(aResponse()
                .withStatus(200)));
    }

    @Test
    void getFileList_ShouldReturnHttpOk() {
        stubFor(post(urlEqualTo("/api/v1/files/users/" + anyLong()))
            .withQueryParam("pageNumber", equalTo(String.valueOf(anyInt())))
            .withQueryParam("pageSize", equalTo(String.valueOf(anyInt())))
            .willReturn(aResponse()
                .withStatus(200)));
    }

    @Test
    void getFileList_ShouldReturnInternalServerError() {
        stubFor(post(urlEqualTo("/api/v1/files/users/" + anyLong()))
            .withQueryParam("pageNumber", equalTo(String.valueOf(anyInt())))
            .withQueryParam("pageSize", equalTo(String.valueOf(anyInt())))
            .willReturn(aResponse()
                .withStatus(500)));
    }

    @Test
    void getFileList_ShouldReturnBadRequest() {
        stubFor(post(urlEqualTo("/api/v1/files/users/" + anyLong()))
            .withQueryParam("pageNumber", equalTo(String.valueOf(anyInt())))
            .withQueryParam("pageSize", equalTo(String.valueOf(anyInt())))
            .willReturn(aResponse()
                .withStatus(400)));
    }

    @Test
    void getFileList_ShouldReturnBadRequest_WhenHttpClientFails() {
        stubFor(post(urlEqualTo("/api/v1/files/users/" + anyLong()))
            .withQueryParam("pageNumber", equalTo(String.valueOf(anyInt())))
            .withQueryParam("pageSize", equalTo(String.valueOf(anyInt())))
            .willReturn(aResponse()
                .withStatus(200)));
    }

    @Test
    void getFile_ShouldReturnHttpOk() {
        stubFor(post(urlEqualTo("/api/v1/files/" + any(UUID.class)))
            .withQueryParam("pageNumber", equalTo(String.valueOf(anyInt())))
            .withQueryParam("pageSize", equalTo(String.valueOf(anyInt())))
            .willReturn(aResponse()
                .withStatus(200)));
    }

    @Test
    void getFile_ShouldReturnInternalServerError() {
        stubFor(post(urlEqualTo("/api/v1/files/" + any(UUID.class)))
            .withQueryParam("pageNumber", equalTo(String.valueOf(anyInt())))
            .withQueryParam("pageSize", equalTo(String.valueOf(anyInt())))
            .willReturn(aResponse()
                .withStatus(500)));
    }

    @Test
    void getFile_ShouldReturnBadRequest() {
        stubFor(post(urlEqualTo("/api/v1/files/" + any(UUID.class)))
            .withQueryParam("pageNumber", equalTo(String.valueOf(anyInt())))
            .withQueryParam("pageSize", equalTo(String.valueOf(anyInt())))
            .willReturn(aResponse()
                .withStatus(400)));
    }

    @Test
    void getFile_ShouldReturnBadRequest_WhenHttpClientFails () {
        stubFor(post(urlEqualTo("/api/v1/files/" + any(UUID.class)))
            .withQueryParam("pageNumber", equalTo(String.valueOf(anyInt())))
            .withQueryParam("pageSize", equalTo(String.valueOf(anyInt())))
            .willReturn(aResponse()
                .withStatus(200)));
    }
}
