package org.example.telegrambot.commands.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.telegrambot.command.workspace.UpdateWorkspace;
import org.example.telegrambot.enums.InputState;
import org.example.telegrambot.model.ButtonInfo;
import org.example.telegrambot.model.KeyboardInfo;
import org.example.telegrambot.service.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateWorkspaceTest {

    private KeyboardInfo keyboardInfo;

    private String successHanle = "Выбери рабочее пространство, у которого хочешь изменить название";

    private String failedHandle = "Не удалось получить информацию";

    private String successUpdate = "Название успешно изменено";

    private String failedUpdate = "Не удалось изменить название";

    @InjectMocks
    private UpdateWorkspace updateCommand;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private Map<Long, InputState> stateMap;

    @Mock
    private Map<Long, UUID> currentWorkspace;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @BeforeEach
    void setUp() {
        updateCommand = new UpdateWorkspace(workspaceService, stateMap, currentWorkspace);
        keyboardInfo = new KeyboardInfo(1, List.of(new ButtonInfo("test", UUID.randomUUID())));
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
    }

    @Test
    void updateWorkspaceCommand_ShouldReturnSuccess_WhenHandleMethodInvoke() {
        when(workspaceService.getWorkspaceList(anyInt(), anyInt(), anyLong())).thenReturn(keyboardInfo);

        SendMessage msg = updateCommand.handle(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(successHanle);
    }

    @Test
    void updateWorkspaceCommand_ShouldReturnFail_WhenHandleMethodInvoke() {
        when(workspaceService.getWorkspaceList(anyInt(), anyInt(), anyLong())).thenReturn(null);

        SendMessage msg = updateCommand.handle(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(failedHandle);
    }

    @Test
    void updateWorkspaceCommand_ShouldReturnSuccess_WhenUpdateMethodInvoke() {
        when(update.message().text()).thenReturn("test");
        when(currentWorkspace.get(anyLong())).thenReturn(UUID.randomUUID());
        when(workspaceService.updateWorkspace(any(UUID.class), anyString())).thenReturn(HttpStatus.OK);

        SendMessage msg = updateCommand.updateName(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(successUpdate);
    }

    @Test
    void updateWorkspaceCommand_ShouldReturnFailed_WhenUpdateMethodInvoke() {
        when(update.message().text()).thenReturn("test");
        when(currentWorkspace.get(anyLong())).thenReturn(UUID.randomUUID());
        when(workspaceService.updateWorkspace(any(UUID.class), anyString())).thenReturn(HttpStatus.BAD_REQUEST);

        SendMessage msg = updateCommand.updateName(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(failedUpdate);
    }

}
