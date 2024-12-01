package org.example.telegrambot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import org.assertj.core.api.Assertions;
import org.example.telegrambot.command.Command;
import org.example.telegrambot.command.workspace.CreateWorkspace;
import org.example.telegrambot.command.workspace.UpdateWorkspace;
import org.example.telegrambot.enums.InputState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wiremock.com.google.common.base.Predicate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandHandlerTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private List<Command> commands;

    @Mock
    private Map<Long, InputState> stateMap;

    @Mock
    private CreateWorkspace createWorkspace;

    @Mock
    private UpdateWorkspace updateWorkspace;

    @InjectMocks
    private CommandHandler commandHandler;

    @Mock
    private Optional<Command> optionalCommand;

    @Test
    void handleState_ShouldInvokeCreateWorkspace() {
        when(update.message().from().id()).thenReturn(1L);
        when(stateMap.containsKey(1L)).thenReturn(true);
        when(stateMap.get(1L)).thenReturn(InputState.WAITING_WORKSPACE_CREATE_NAME);

        commandHandler.handle(update);

        verify(createWorkspace).create(update);
    }

    @Test
    void handleState_ShouldInvokeUpdateWorkspace() {
        when(update.message().from().id()).thenReturn(1L);
        when(stateMap.containsKey(anyLong())).thenReturn(true);
        when(stateMap.get(anyLong())).thenReturn(InputState.WAITING_WORKSPACE_UPDATE_NAME);

        commandHandler.handle(update);

        verify(updateWorkspace).updateName(update);
    }

    @Test
    void handle_ShouldReturnNotFound() {
        when(update.message().chat().id()).thenReturn(1L);
        when(stateMap.containsKey(anyLong())).thenReturn(false);

        SendMessage msg = commandHandler.handle(update);
        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo("Команда не найдена");
    }

    @Test
    void handle_ShouldHandleCommand() {
        when(stateMap.containsKey(anyLong())).thenReturn(false);
        when(commands.stream().filter(any()).findFirst()).thenReturn(optionalCommand);
        when(optionalCommand.isPresent()).thenReturn(true);
        when(optionalCommand.get()).thenReturn(mock(Command.class));

        commandHandler.handle(update);

        verify(optionalCommand.get()).handle(update);
    }
}
