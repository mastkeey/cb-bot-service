package ru.mastkey.telegrambot.commands.file;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mastkey.telegrambot.command.file.DeleteFile;
import ru.mastkey.telegrambot.model.ButtonInfo;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.DocumentService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteFileTest {

    @InjectMocks
    private DeleteFile deleteFile;

    @Mock
    private DocumentService documentService;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    private KeyboardInfo keyboardInfo;

    @Mock
    private Map<Long, UUID> currentWorkspace;

    @BeforeEach
    void setUp() {
        keyboardInfo = new KeyboardInfo(1, List.of(new ButtonInfo("test", UUID.randomUUID())));

        when(update.message().chat().id()).thenReturn(1L);
        when(update.message().from().id()).thenReturn(1L);
        when(currentWorkspace.get(anyLong())).thenReturn(UUID.randomUUID());
    }

    @Test
    void handle_ShouldReturnSuccessMessage_WhenKeyboardInfoIsNotEmpty() {
        when(documentService.getFileList(anyLong(), any(UUID.class), anyInt(), anyInt())).thenReturn(keyboardInfo);

        SendMessage response = deleteFile.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Выбери файл который хочешь удалить");
    }

    @Test
    void handle_ShouldReturnEmptyFilesMessage_WhenKeyboardInfoHasNoButtons() {
        KeyboardInfo emptyKeyboardInfo = new KeyboardInfo(1, List.of());
        when(documentService.getFileList(anyLong(), any(UUID.class), anyInt(), anyInt())).thenReturn(emptyKeyboardInfo);

        SendMessage response = deleteFile.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("В рабочем пространстве отсутствуют файлы");
    }

    @Test
    void handle_ShouldReturnFailedMessage_WhenKeyboardInfoIsNull() {
        when(documentService.getFileList(anyLong(), any(UUID.class), anyInt(), anyInt())).thenReturn(null);

        SendMessage response = deleteFile.handle(update);

        assertThat(response.getParameters().get("chat_id")).isEqualTo(1L);
        assertThat(response.getParameters().get("text")).isEqualTo("Не удалось получить информацию");
    }
}