package ru.mastkey.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import ru.mastkey.telegrambot.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mastkey.telegrambot.util.Constants;

import java.util.List;
import java.util.Map;

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

    @Test
    void loadVoice_ShouldReturnCorrectMessage_WhenFileSizeIsWithinLimit() {
        String fileId = "testVoiceId";
        File file = mock(File.class);
        GetFileResponse fileResponse = mock(GetFileResponse.class);

        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
        when(update.message().voice().fileId()).thenReturn(fileId);
        when(update.message().voice().fileSize()).thenReturn(50000L);

        when(telegramBot.execute(any(GetFile.class))).thenReturn(fileResponse);
        when(fileResponse.file()).thenReturn(file);

        when(documentService.addFile(
                eq(1L),
                argThat(fileName -> fileName.matches("VM_\\d{4}-\\d{2}-\\d{2}_\\d{2}:\\d{2}:\\d{2}\\.mp3")),
                eq(file)
        )).thenReturn(List.of("testFileName.mp3"));

        SendMessage result = documentHandler.loadVoice(update);

        assertThat(result.getParameters().get("chat_id")).isEqualTo(1L);
    }

    @Test
    void loadVoice_ShouldReturnTooLargeMessage_WhenFileSizeExceedsLimit() {
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().voice().fileSize()).thenReturn(50_000_000L);

        SendMessage result = documentHandler.loadVoice(update);

        assertThat(result.getParameters().get("text")).isEqualTo(Constants.FILE_TOO_LARGE);
    }

    @Test
    void loadShortVideo_ShouldReturnCorrectMessage_WhenFileSizeIsWithinLimit() {
        String fileId = "testVideoId";
        File file = mock(File.class);
        GetFileResponse fileResponse = mock(GetFileResponse.class);

        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
        when(update.message().videoNote().fileId()).thenReturn(fileId);
        when(update.message().videoNote().fileSize()).thenReturn(50000L);

        when(telegramBot.execute(any(GetFile.class))).thenReturn(fileResponse);
        when(fileResponse.file()).thenReturn(file);

        when(documentService.addFile(
                eq(1L),
                argThat(fileName -> fileName.matches("CV_\\d{4}-\\d{2}-\\d{2}_\\d{2}:\\d{2}:\\d{2}\\.mp4")),
                eq(file)
        )).thenReturn(List.of("testFileName.mp4"));

        SendMessage result = documentHandler.loadShortVideo(update);

        assertThat(result.getParameters().get("chat_id")).isEqualTo(1L);
    }

    @Test
    void loadShortVideo_ShouldReturnTooLargeMessage_WhenFileSizeExceedsLimit() {
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().videoNote().fileSize()).thenReturn(50_000_000L);

        SendMessage result = documentHandler.loadShortVideo(update);

        assertThat(result.getParameters().get("text")).isEqualTo(Constants.FILE_TOO_LARGE);
    }

    @Test
    void generateFileName_ShouldReturnFormattedFileName() {
        String prefix = "VM";
        String extension = "mp3";

        String result = documentHandler.generateFileName(prefix, extension);

        assertThat(result).matches("^VM_\\d{4}-\\d{2}-\\d{2}_\\d{2}:\\d{2}:\\d{2}\\.mp3$");
    }
}
