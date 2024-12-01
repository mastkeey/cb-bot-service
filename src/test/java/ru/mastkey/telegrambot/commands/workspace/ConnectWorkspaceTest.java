package ru.mastkey.telegrambot.commands.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mastkey.telegrambot.command.workspace.ConnectWorkspace;
import ru.mastkey.telegrambot.enums.InputState;
import ru.mastkey.telegrambot.service.UserService;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConnectWorkspaceTest {

    @InjectMocks
    private ConnectWorkspace connectWorkspace;

    @Mock
    private Map<Long, InputState> stateMap;

    @Mock
    private UserService userService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @BeforeEach
    void setUp() {
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
    }

    @Test
    void handle_ShouldSetStateAndReturnWaitingMessage() {
        SendMessage response = connectWorkspace.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Введите UUID рабочего пространства которым с вами поделились");
    }

    @Test
    void connect_ShouldReturnSuccessMessage_WhenConnectionIsSuccessful() {
        when(update.message().text()).thenReturn(UUID.randomUUID().toString());
        when(userService.connectWorkspace(anyLong(), any(UUID.class))).thenReturn(org.springframework.http.HttpStatus.OK);

        SendMessage response = connectWorkspace.connect(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Вы успешно подключились к чужому рабочему пространству");
    }

    @Test
    void connect_ShouldReturnFailedMessage_WhenConnectionFails() {
        when(update.message().text()).thenReturn(UUID.randomUUID().toString());
        when(userService.connectWorkspace(anyLong(), any(UUID.class))).thenReturn(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);

        SendMessage response = connectWorkspace.connect(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Не удалось подключиться к чужому рабочему пространству");
    }

    @Test
    void connect_ShouldReturnIncorrectInputMessage_WhenUuidIsInvalid() {
        when(update.message().text()).thenReturn("invalid-uuid");

        SendMessage response = connectWorkspace.connect(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Некорректный ввод, попробуйте снова");
    }
}