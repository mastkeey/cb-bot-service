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
import ru.mastkey.model.WorkspaceResponse;
import ru.mastkey.telegrambot.command.workspace.ShareWorkspace;
import ru.mastkey.telegrambot.service.WorkspaceService;
import ru.mastkey.telegrambot.util.Constants;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShareWorkspaceTest {

    @InjectMocks
    private ShareWorkspace shareWorkspace;

    @Mock
    private WorkspaceService workspaceService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @BeforeEach
    void setUp() {
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
    }

    @Test
    void handle_ShouldReturnWorkspacesMessage_WhenWorkspacesAreAvailable() {
        WorkspaceResponse workspace1 = new WorkspaceResponse();
        workspace1.setName("Workspace1");
        workspace1.setWorkspaceId(UUID.randomUUID());
        when(workspaceService.getAllWorkspace(anyLong())).thenReturn(List.of(workspace1));

        SendMessage response = shareWorkspace.handle(update);

        String expectedWorkspaces = String.format(Constants.SHARE_SUCCESS,
                String.format("%s - ```%s```", workspace1.getName(), workspace1.getWorkspaceId())
        );

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo(expectedWorkspaces);
    }

    @Test
    void handle_ShouldReturnInformationNotFoundMessage_WhenNoWorkspacesAvailable() {
        when(workspaceService.getAllWorkspace(anyLong())).thenReturn(List.of());

        SendMessage response = shareWorkspace.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Не удалось получить информацию");
    }
}