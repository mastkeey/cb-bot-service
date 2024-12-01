package org.example.telegrambot.commands.file;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.example.telegrambot.command.file.DownloadFile;
import org.example.telegrambot.model.ButtonInfo;
import org.example.telegrambot.model.KeyboardInfo;
import org.example.telegrambot.service.DocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DownloadFileTest {

    private KeyboardInfo keyboardInfo;

    private String success = "Выбери файл который хочешь получить";

    private String failed = "Не удалось получить список файлов";

    @InjectMocks
    private DownloadFile downloadFile;

    @Mock
    private DocumentService documentService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @BeforeEach
    void setUp() {
        keyboardInfo = new KeyboardInfo(1, List.of(new ButtonInfo("test", UUID.randomUUID())));
        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
    }

    @Test
    void downloadFileCommand_ShouldReturnSuccess_WhenHandleMethodInvoke() {
        when(documentService.getFileList(anyInt(), anyInt(), anyLong())).thenReturn(keyboardInfo);

        SendMessage msg = downloadFile.handle(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(success);
    }

    @Test
    void downloadFileCommand_ShouldReturnFailed_WhenHandleMethodInvoke() {
        when(documentService.getFileList(anyInt(), anyInt(), anyLong())).thenReturn(null);

        SendMessage msg = downloadFile.handle(update);

        Map<String, Object> parameters = msg.getParameters();

        assertThat(parameters.get("chat_id")).isEqualTo(1L);
        assertThat(parameters.get("text")).isEqualTo(failed);
    }
}
