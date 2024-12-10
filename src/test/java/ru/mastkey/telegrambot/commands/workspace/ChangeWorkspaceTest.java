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
import ru.mastkey.telegrambot.command.workspace.ChangeWorkspace;
import ru.mastkey.telegrambot.model.ButtonInfo;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.WorkspaceService;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChangeWorkspaceTest {

    @InjectMocks
    private ChangeWorkspace changeWorkspace;

    @Mock
    private WorkspaceService workspaceService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    private KeyboardInfo keyboardInfo;

    @BeforeEach
    void setUp() {
        keyboardInfo = new KeyboardInfo(1, List.of(new ButtonInfo("Workspace 1", UUID.randomUUID())));

        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
    }

    @Test
    void handle_ShouldReturnSuccessMessage_WhenWorkspaceListIsNotEmpty() {
        when(workspaceService.getWorkspaceList(anyLong(), anyInt(), anyInt())).thenReturn(keyboardInfo);

        SendMessage response = changeWorkspace.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Выберите рабочее пространство, которое хотите сделать активным");
    }

    @Test
    void handle_ShouldReturnEmptyMessage_WhenWorkspaceListHasNoButtons() {
        KeyboardInfo emptyKeyboardInfo = new KeyboardInfo(1, List.of());
        when(workspaceService.getWorkspaceList(anyLong(), anyInt(), anyInt())).thenReturn(emptyKeyboardInfo);

        SendMessage response = changeWorkspace.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("У вас отсутствуют рабочие пространства");
    }

    @Test
    void handle_ShouldReturnInformationNotFoundMessage_WhenWorkspaceListIsNull() {
        when(workspaceService.getWorkspaceList(anyLong(), anyInt(), anyInt())).thenReturn(null);

        SendMessage response = changeWorkspace.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Не удалось получить информацию");
    }
}