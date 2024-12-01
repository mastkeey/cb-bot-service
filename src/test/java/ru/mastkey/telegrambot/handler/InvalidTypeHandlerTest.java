package ru.mastkey.telegrambot.handler;

import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mastkey.telegrambot.util.Constants;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvalidTypeHandlerTest {

    private InvalidTypeHandler invalidTypeHandler;

    @BeforeEach
    void setUp() {
        invalidTypeHandler = new InvalidTypeHandler();
    }

    @Test
    void handle_ShouldReturnInvalidPhotoOrVideoMessage_WhenPhotoExists() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(12345L);
        when(message.photo()).thenReturn(new PhotoSize[]{});

        SendMessage result = invalidTypeHandler.handle(update);

        assertThat(result.getParameters().get("chat_id")).isEqualTo(12345L);
        assertThat(result.getParameters().get("text")).isEqualTo(Constants.INVALID_PHOTO_OR_VIDEO);
    }

    @Test
    void handle_ShouldReturnInvalidPhotoOrVideoMessage_WhenVideoExists() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(12345L);
        when(message.photo()).thenReturn(null);
        when(message.video()).thenReturn(new Video());

        SendMessage result = invalidTypeHandler.handle(update);

        assertThat(result.getParameters().get("chat_id")).isEqualTo(12345L);
        assertThat(result.getParameters().get("text")).isEqualTo(Constants.INVALID_PHOTO_OR_VIDEO);
    }

    @Test
    void handle_ShouldReturnInvalidTypeMessage_WhenUnsupportedType() {
        Update update = mock(Update.class);
        Message message = mock(Message.class);
        Chat chat = mock(Chat.class);

        when(update.message()).thenReturn(message);
        when(message.chat()).thenReturn(chat);
        when(chat.id()).thenReturn(12345L);
        when(message.photo()).thenReturn(null);
        when(message.video()).thenReturn(null);

        SendMessage result = invalidTypeHandler.handle(update);

        assertThat(result.getParameters().get("chat_id")).isEqualTo(12345L);
        assertThat(result.getParameters().get("text")).isEqualTo(Constants.INVALID_TYPE);
    }
}