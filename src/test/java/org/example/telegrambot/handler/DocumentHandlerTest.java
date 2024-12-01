package org.example.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import org.assertj.core.api.Assertions;
import org.example.telegrambot.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentHandlerTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @Mock
    private Map<Long, Integer> latestMsg;

    @Mock
    private DocumentService documentService;

    @Mock
    private TelegramBot telegramBot;

    @InjectMocks
    private DocumentHandler documentHandler;

    @Test
    void deleteLastMsg_ShouldInvokeExecute() {
        when(latestMsg.containsKey(anyLong())).thenReturn(true);
        when(latestMsg.get(anyLong())).thenReturn(1);

        documentHandler.deleteLastMsg(1L);

        verify(telegramBot).execute(any(DeleteMessage.class));
    }

    @Test
    void deleteLastMsg_ShouldNotInvokeExecute() {
        when(latestMsg.containsKey(anyLong())).thenReturn(false);

        documentHandler.deleteLastMsg(1L);

        verify(telegramBot, times(0)).execute(any(DeleteMessage.class));
    }

    @Test
    void saveLastMsg_ShouldSaveMessageId() {
        documentHandler.saveLastMsg(1L, 1);

        verify(latestMsg).put(1L, 1);
    }

    @Test
    void loadDocument_ShouldReturnCorrectMessage() {
        File file = mock(File.class);
        GetFileResponse fileResponse = mock(GetFileResponse.class);
        String fileId = "test";
        String fileName = "testFile.txt";
        List<String> names = List.of(fileName);
        String messageText = "Вы прикрепили 1 файл\ntestFile.txt";

        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
        when(update.message().document().fileId()).thenReturn(fileId);
        when(update.message().document().fileName()).thenReturn(fileName);
        when(telegramBot.execute(any(GetFile.class))).thenReturn(fileResponse);
        when(fileResponse.file()).thenReturn(file);
        when(documentService.addFile(anyLong(), anyString(), any(File.class))).thenReturn(names);

        SendMessage sendMessage = documentHandler.loadDocument(update);
        Map<String, Object> parameters = sendMessage.getParameters();

        InlineKeyboardMarkup keyboardMarkup = ((InlineKeyboardMarkup) parameters.get("reply_markup"));

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(messageText);
        assertThat(keyboardMarkup.inlineKeyboard()[0][0].text()).isEqualTo("Отправить");
    }

    @Test
    void loadDocument_ShouldReturnCorrectMessage_WhenCantLoadFile() {
        File file = mock(File.class);
        GetFileResponse fileResponse = mock(GetFileResponse.class);
        String fileId = "test";
        String fileName = "testFile.txt";
        List<String> names = List.of();
        String messageText = "Не удалось прикрепить файл";

        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
        when(update.message().document().fileId()).thenReturn(fileId);
        when(update.message().document().fileName()).thenReturn(fileName);
        when(telegramBot.execute(any(GetFile.class))).thenReturn(fileResponse);
        when(fileResponse.file()).thenReturn(file);
        when(documentService.addFile(anyLong(), anyString(), any(File.class))).thenReturn(names);

        SendMessage sendMessage = documentHandler.loadDocument(update);
        Map<String, Object> parameters = sendMessage.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(messageText);
    }
}
