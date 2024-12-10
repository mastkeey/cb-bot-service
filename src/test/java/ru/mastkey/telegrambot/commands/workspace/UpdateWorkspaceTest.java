package ru.mastkey.telegrambot.commands.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mastkey.telegrambot.command.workspace.UpdateWorkspace;
import ru.mastkey.telegrambot.enums.Action;
import ru.mastkey.telegrambot.enums.InputState;
import ru.mastkey.telegrambot.enums.Type;
import ru.mastkey.telegrambot.model.ButtonInfo;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.WorkspaceService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateWorkspaceTest {

    @InjectMocks
    private UpdateWorkspace updateWorkspace;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private Map<Long, InputState> stateMap;

    @Mock
    private Map<Long, UUID> currentWorkspaceToUpdateName;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @BeforeEach
    void setUp() {
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);

        UUID workspaceId = UUID.randomUUID(); // Уникальный ID для теста
        when(currentWorkspaceToUpdateName.get(1L)).thenReturn(workspaceId);
    }

    @Test
    void handle_ShouldReturnSuccessMessage_WhenWorkspaceListIsNotEmpty() {
        KeyboardInfo workspaceInfo = new KeyboardInfo(1, List.of(new ButtonInfo("Workspace 1", UUID.randomUUID())));
        when(workspaceService.getWorkspaceList(anyLong(), anyInt(), anyInt())).thenReturn(workspaceInfo);

        SendMessage response = updateWorkspace.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Выбери рабочее пространство, у которого хочешь изменить название");
    }

    @Test
    void handle_ShouldReturnEmptyWorkspacesMessage_WhenWorkspaceListHasNoButtons() {
        KeyboardInfo emptyWorkspaceInfo = new KeyboardInfo(1, List.of());
        when(workspaceService.getWorkspaceList(anyLong(), anyInt(), anyInt())).thenReturn(emptyWorkspaceInfo);

        SendMessage response = updateWorkspace.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("У вас отсутствуют рабочие пространства");
    }

    @Test
    void handle_ShouldReturnInformationNotFoundMessage_WhenWorkspaceListIsNull() {
        when(workspaceService.getWorkspaceList(anyLong(), anyInt(), anyInt())).thenReturn(null);

        SendMessage response = updateWorkspace.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Не удалось получить информацию");
    }

    @Test
    void updateName_ShouldReturnNameTooLongMessage_WhenWorkspaceNameExceedsMaxLength() {
        String longWorkspaceName = "A".repeat(41);
        when(update.message().text()).thenReturn(longWorkspaceName);

        SendMessage response = updateWorkspace.updateName(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Длина имени рабочего пространства не должна превышать 40 символов");
    }

    @AfterEach
    void tearDown() {
        reset(workspaceService, stateMap, currentWorkspaceToUpdateName, update);
    }
}