package ru.mastkey.telegrambot.handler;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import ru.mastkey.telegrambot.enums.InputState;
import ru.mastkey.telegrambot.service.DocumentService;
import ru.mastkey.telegrambot.service.WorkspaceService;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.mastkey.telegrambot.util.Constants.*;


@ExtendWith(MockitoExtension.class)
class CallbackHandlerTest {

    @Mock
    private DocumentService documentService;

    @Mock
    private DocumentHandler documentHandler;

    @Mock
    private WorkspaceService workspaceService;

    @Mock
    private Map<Long, InputState> stateMap;

    @Mock
    private Map<Long, UUID> currentWorkspaceToUpdateName;

    @Mock
    private Map<Long, UUID> currentWorkspace;

    @InjectMocks
    private CallbackHandler callbackHandler;

    @Mock
    private CallbackQuery callbackQuery;

    @BeforeEach
    void setUp() {
        var mockUser = mock(User.class);
        when(mockUser.id()).thenReturn(1L);
        when(callbackQuery.from()).thenReturn(mockUser);
        when(callbackQuery.from().id()).thenReturn(1L);
    }

    @Test
    void handle_ShouldReturnUnknownCommand_WhenActionIsUnknown() {
        when(callbackQuery.maybeInaccessibleMessage()).thenReturn(mock(Message.class));
        when(callbackQuery.maybeInaccessibleMessage().chat()).thenReturn(mock(Chat.class));
        when(callbackQuery.data()).thenReturn("unknown");
        when(callbackQuery.from().id()).thenReturn(1L);
        when(callbackQuery.maybeInaccessibleMessage().chat().id()).thenReturn(1L);

        BaseRequest<?, ?> response = callbackHandler.handle(callbackQuery);

        assertThat(response).isInstanceOf(SendMessage.class);
        assertThat(((SendMessage) response).getParameters().get("text")).isEqualTo(UNKNOWN_COMMAND);
    }

    @Test
    void handle_ShouldCallHandleArrow_WhenActionIsLeft() {
        when(callbackQuery.maybeInaccessibleMessage()).thenReturn(mock(Message.class));
        when(callbackQuery.maybeInaccessibleMessage().chat()).thenReturn(mock(Chat.class));

        when(callbackQuery.data()).thenReturn("left/0/FILE");
        when(callbackQuery.from().id()).thenReturn(1L);
        when(callbackQuery.maybeInaccessibleMessage().chat().id()).thenReturn(1L);

        BaseRequest<?, ?> response = callbackHandler.handle(callbackQuery);

        assertThat(response).isInstanceOf(SendMessage.class);
        assertThat(((SendMessage) response).getParameters().get("text")).isEqualTo(INFORMATION_NOT_FOUND);
    }

    @Test
    void handle_ShouldCallHandleSend_WhenActionIsSend() {
        when(callbackQuery.maybeInaccessibleMessage()).thenReturn(mock(Message.class));
        when(callbackQuery.maybeInaccessibleMessage().chat()).thenReturn(mock(Chat.class));

        when(callbackQuery.data()).thenReturn("send");
        when(callbackQuery.from().id()).thenReturn(1L);
        when(callbackQuery.maybeInaccessibleMessage().chat().id()).thenReturn(1L);
        when(documentService.uploadFiles(1L)).thenReturn(true);

        BaseRequest<?, ?> response = callbackHandler.handle(callbackQuery);

        assertThat(response).isInstanceOf(SendMessage.class);
        assertThat(((SendMessage) response).getParameters().get("text")).isEqualTo(FILE_UPLOAD_SUCCESS);
    }

    @Test
    void handle_ShouldCallHandleUpdate_WhenActionIsUpdate() {

        UUID workspaceId = UUID.randomUUID();
        when(callbackQuery.data()).thenReturn("UPDATE/WORKSPACE/" + workspaceId);
        when(callbackQuery.from().id()).thenReturn(1L);

        BaseRequest<?, ?> response = callbackHandler.handle(callbackQuery);

        assertThat(response).isInstanceOf(SendMessage.class);
        assertThat(((SendMessage) response).getParameters().get("text")).isEqualTo(WAITING_WORKSPACE_NAME_TO_UPDATE);
    }

    @Test
    void handle_ShouldCallHandleDelete_WhenActionIsDeleteForWorkspace() {

        UUID workspaceId = UUID.randomUUID();
        when(callbackQuery.data()).thenReturn("DELETE/WORKSPACE/" + workspaceId);
        when(callbackQuery.from().id()).thenReturn(1L);
        when(workspaceService.deleteWorkspace(anyLong(), eq(workspaceId))).thenReturn(HttpStatus.OK);

        BaseRequest<?, ?> response = callbackHandler.handle(callbackQuery);

        assertThat(response).isInstanceOf(SendMessage.class);
        assertThat(((SendMessage) response).getParameters().get("text")).isEqualTo(WORKSPACE_DELETE_SUCCESS);
    }

    @Test
    void handle_ShouldCallHandleSelect_WhenActionIsSelect() {
        UUID workspaceId = UUID.randomUUID();

        when(callbackQuery.data()).thenReturn("SELECT/WORKSPACE/" + workspaceId);
        when(callbackQuery.from().id()).thenReturn(1L);

        BaseRequest<?, ?> response = callbackHandler.handle(callbackQuery);

        assertThat(response).isInstanceOf(SendMessage.class);
        assertThat(((SendMessage) response).getParameters().get("text")).isEqualTo(WORKSPACE_SELECT_SUCCESS);
    }

    @Test
    void handle_ShouldCallHandleGet_WhenActionIsGetAndFileExists() throws Exception {
        when(callbackQuery.maybeInaccessibleMessage()).thenReturn(mock(Message.class));
        when(callbackQuery.maybeInaccessibleMessage().chat()).thenReturn(mock(Chat.class));

        UUID fileId = UUID.randomUUID();
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
        when(callbackQuery.data()).thenReturn("GET/FILE/" + fileId);
        when(callbackQuery.from().id()).thenReturn(1L);
        when(documentService.getFile(1L, null, fileId)).thenReturn(mockFile);
        when(callbackQuery.maybeInaccessibleMessage().chat().id()).thenReturn(1L);

        BaseRequest<?, ?> response = callbackHandler.handle(callbackQuery);

        assertThat(response).isInstanceOf(SendDocument.class);
        assertThat(((SendDocument) response).getParameters().get("chat_id")).isEqualTo(1L);
    }

    @Test
    void handle_ShouldCallHandleGet_WhenActionIsGetAndFileDoesNotExist() throws Exception {
        when(callbackQuery.maybeInaccessibleMessage()).thenReturn(mock(Message.class));

        when(callbackQuery.maybeInaccessibleMessage().chat()).thenReturn(mock(Chat.class));

        UUID fileId = UUID.randomUUID();
        when(callbackQuery.data()).thenReturn("GET/FILE/" + fileId);
        when(callbackQuery.from().id()).thenReturn(1L);
        when(documentService.getFile(1L, null, fileId)).thenReturn(null);
        when(callbackQuery.maybeInaccessibleMessage().chat().id()).thenReturn(1L);

        BaseRequest<?, ?> response = callbackHandler.handle(callbackQuery);

        assertThat(response).isInstanceOf(SendMessage.class);
        assertThat(((SendMessage) response).getParameters().get("text")).isEqualTo(FILE_GET_FAILED);
    }
}