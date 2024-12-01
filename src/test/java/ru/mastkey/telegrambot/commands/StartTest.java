package ru.mastkey.telegrambot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import ru.mastkey.telegrambot.command.Start;
import ru.mastkey.telegrambot.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.mastkey.telegrambot.util.Constants;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StartTest {

    private Start start;

    @Mock
    private UserService userService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    private String failed = "Не удалось зарегистрироваться";

    @BeforeEach
    void setUp() {
        start = new Start(userService);
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
    }

    @Test
    public void startCommand_ShouldReturnSuccess() {
        when(userService.createUser(anyLong())).thenReturn(HttpStatus.OK);

        SendMessage msg = start.handle(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(Constants.HELLO_MESSAGE);
    }

    @Test
    public void startCommand_ShouldReturnFail() {
        when(userService.createUser(anyLong())).thenReturn(HttpStatus.BAD_REQUEST);

        SendMessage msg = start.handle(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(failed);
    }
}
