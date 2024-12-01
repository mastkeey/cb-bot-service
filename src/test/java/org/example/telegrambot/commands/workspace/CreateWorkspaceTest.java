package org.example.telegrambot.commands.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

import org.example.telegrambot.command.workspace.CreateWorkspace;
import org.example.telegrambot.enums.InputState;
import org.example.telegrambot.service.WorkspaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateWorkspaceTest {

    private String helloMsg = "Введите название рабочего пространства";

    private String success = "Рабочее пространство %s успешно создано";

    private String failed = "Не удалось создать рабочее пространство с названием %s";

    @InjectMocks
    private CreateWorkspace create;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private Map<Long, InputState> stateMap;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @BeforeEach
    void setUp() {
        create = new CreateWorkspace(workspaceService, stateMap);
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
    }

    @Test
    void createCommand_ShouldReturnHelloMsg_WhenHandleMethodInvoke() {
        SendMessage msg = create.handle(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(helloMsg);
    }

    @Test
    void createCommand_ShouldReturnSuccess_WhenCreateMethodInvoke() {
        when(update.message().text()).thenReturn("test_create");
        when(workspaceService.createWorkspace(anyString(), anyLong())).thenReturn(HttpStatus.OK);

        SendMessage msg = create.create(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(String.format(success, "test_create"));
    }

    @Test
    void createCommand_ShouldReturnFailed_WhenCreateMethodInvoke() {
        when(update.message().text()).thenReturn("test_create");
        when(workspaceService.createWorkspace(anyString(), anyLong())).thenReturn(HttpStatus.BAD_REQUEST);

        SendMessage msg = create.create(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(String.format(failed, "test_create"));
    }

}
