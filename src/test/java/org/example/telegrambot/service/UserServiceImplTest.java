package org.example.telegrambot.service;

import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith({MockitoExtension.class, WireMockExtension.class})
@WireMockTest(httpPort = 8080)
public class UserServiceImplTest {

    /*String json = """
        {
            "userId" : 1,
            "chatId" : 1,
            "username" : "test"
        }
        """;

    private CloseableHttpClient httpClient;

    private UserServiceImpl userServiceImpl;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ApiConfig apiConfig;


    @BeforeEach
    void setUp() {
        httpClient = spy(HttpClients.createDefault());
        userServiceImpl = new UserServiceImpl(apiConfig, httpClient, objectMapper);
        when(apiConfig.getUserUrl()).thenReturn("http://localhost:8080/api/v1/users");
    }

    @Test
    void createUser_ShouldReturnHttpOk() throws IOException {
        stubFor(post(urlEqualTo("/api/v1/users"))
            .withHeader("Content-Type", equalTo("application/json"))
            .willReturn(aResponse()
                .withStatus(200)));

        when(objectMapper.writeValueAsString(any())).thenReturn(json);

        HttpStatus result = userServiceImpl.createUser(1L, 1L, "test");

        assertThat(result).isEqualTo(HttpStatus.OK);
        WireMock.verify(1, postRequestedFor(urlEqualTo("/api/v1/users"))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void createUser_ShouldReturnInternalServerError() throws JsonProcessingException {
        stubFor(post(urlEqualTo("/api/v1/users"))
            .willReturn(aResponse()
                .withStatus(500)));

        when(objectMapper.writeValueAsString(any())).thenReturn(json);

        HttpStatus result = userServiceImpl.createUser(1L, 1L, "test");

        assertThat(result).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        WireMock.verify(1, postRequestedFor(urlEqualTo("/api/v1/users"))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void createUser_ShouldReturnBadRequest() throws JsonProcessingException {
        stubFor(post(urlEqualTo("/api/v1/users"))
            .willReturn(aResponse()
                .withStatus(400)));

        when(objectMapper.writeValueAsString(any())).thenReturn(json);

        HttpStatus result = userServiceImpl.createUser(1L, 1L, "test");

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        WireMock.verify(1, postRequestedFor(urlEqualTo("/api/v1/users"))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenHttpClientFails() throws IOException {
        stubFor(post(urlEqualTo("/api/v1/users"))
            .willReturn(aResponse()
                .withStatus(200)));

        when(objectMapper.writeValueAsString(any())).thenReturn(json);
        doThrow(ClientProtocolException.class)
            .when(httpClient)
            .execute(any(HttpPost.class));

        HttpStatus result = userServiceImpl.createUser(1L, 1L, "test");

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        WireMock.verify(0, postRequestedFor(urlEqualTo("/api/v1/users"))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void createUser_ShouldReturnBadRequest_WhenJsonParseFails() throws IOException {
        stubFor(post(urlEqualTo("/api/v1/users"))
            .willReturn(aResponse()
                .withStatus(200)));

        when(objectMapper.writeValueAsString(any())).thenThrow(JsonProcessingException.class);

        HttpStatus result = userServiceImpl.createUser(1L, 1L, "test");

        assertThat(result).isEqualTo(HttpStatus.BAD_REQUEST);
        WireMock.verify(0, postRequestedFor(urlEqualTo("/api/v1/users"))
            .withHeader("Content-Type", equalTo("application/json")));
    }*/
}
