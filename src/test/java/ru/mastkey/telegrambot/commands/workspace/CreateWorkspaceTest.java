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
import ru.mastkey.telegrambot.command.workspace.CreateWorkspace;
import ru.mastkey.telegrambot.enums.InputState;
import ru.mastkey.telegrambot.service.WorkspaceService;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateWorkspaceTest {

    @InjectMocks
    private CreateWorkspace createWorkspace;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private Map<Long, InputState> stateMap;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @BeforeEach
    void setUp() {
        when(update.message().chat().id()).thenReturn(1L);
    }

    @Test
    void handle_ShouldSetStateAndReturnWaitingMessage() {
        SendMessage response = createWorkspace.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Введите название рабочего пространства");
    }

    @Test
    void create_ShouldReturnSuccessMessage_WhenWorkspaceIsCreatedSuccessfully() {
        String workspaceName = "TestWorkspace";
        when(update.message().text()).thenReturn(workspaceName);
        when(workspaceService.createWorkspace(anyLong(), anyString())).thenReturn(org.springframework.http.HttpStatus.CREATED);

        SendMessage response = createWorkspace.create(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo(String.format("Рабочее пространство %s успешно создано".formatted(workspaceName)));
    }

    @Test
    void create_ShouldReturnFailedMessage_WhenWorkspaceCreationFails() {
        String workspaceName = "TestWorkspace";
        when(update.message().text()).thenReturn(workspaceName);
        when(workspaceService.createWorkspace(anyLong(), anyString())).thenReturn(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);

        SendMessage response = createWorkspace.create(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo(String.format("Не удалось создать рабочее пространство с названием %s".formatted(workspaceName)));
    }

    @Test
    void create_ShouldReturnNameTooLongMessage_WhenWorkspaceNameExceedsMaxLength() {
        String longWorkspaceName = "A".repeat(101); // Assuming MAX_WORKSPACE_NAME_LENGTH = 100
        when(update.message().text()).thenReturn(longWorkspaceName);

        SendMessage response = createWorkspace.create(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Длина имени рабочего пространства не должна превышать 30 символов");
    }
}