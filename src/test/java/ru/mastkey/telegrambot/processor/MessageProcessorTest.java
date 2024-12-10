package ru.mastkey.telegrambot.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import ru.mastkey.telegrambot.MessageProcessor;
import ru.mastkey.telegrambot.handler.CallbackHandler;
import ru.mastkey.telegrambot.handler.CommandHandler;
import ru.mastkey.telegrambot.handler.DocumentHandler;
import ru.mastkey.telegrambot.handler.InvalidTypeHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageProcessorTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @Mock
    private Message message;

    @Mock
    private Document document;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private SendResponse sendResponse;

    @Mock
    private SendMessage sendMessage;

    @Mock
    private CallbackQuery callbackQuery;

    @Mock
    private TelegramBot telegramBot;

    @Mock
    private CommandHandler commandHandler;

    @Mock
    private CallbackHandler callbackHandler;

    @Mock
    private DocumentHandler documentHandler;

    @Mock
    private InvalidTypeHandler invalidTypeHandler;

    @InjectMocks
    private MessageProcessor messageProcessor;

    @Test
    void process_ShouldHandleTextMessage() {
        when(update.message()).thenReturn(message);
        when(message.text()).thenReturn("Some text");

        int result = messageProcessor.process(List.of(update));

        assertThat(result).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);
        verify(commandHandler).handle(update);
    }

    @Test
    void process_ShouldHandleVoiceMessage() {
        Voice voice = mock(Voice.class);
        when(update.message()).thenReturn(message);
        when(message.voice()).thenReturn(voice);
        when(documentHandler.loadVoice(update)).thenReturn(sendMessage);
        when(telegramBot.execute(sendMessage)).thenReturn(sendResponse);
        when(sendResponse.message().chat().id()).thenReturn(1L);
        when(sendResponse.message().messageId()).thenReturn(123);

        int result = messageProcessor.process(List.of(update));

        assertThat(result).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);
        verify(documentHandler).loadVoice(update);
        verify(documentHandler).saveLastMsg(1L, 123);
    }

    @Test
    void process_ShouldHandleVideoNote() {
        VideoNote videoNote = mock(VideoNote.class);
        when(update.message()).thenReturn(message);
        when(message.videoNote()).thenReturn(videoNote);
        when(documentHandler.loadShortVideo(update)).thenReturn(sendMessage);
        when(telegramBot.execute(sendMessage)).thenReturn(sendResponse);
        when(sendResponse.message().chat().id()).thenReturn(2L);
        when(sendResponse.message().messageId()).thenReturn(456);

        int result = messageProcessor.process(List.of(update));

        assertThat(result).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);
        verify(documentHandler).loadShortVideo(update);
        verify(documentHandler).saveLastMsg(2L, 456);
    }

    @Test
    void process_ShouldHandleDocumentMessage() {
        when(update.message()).thenReturn(message);
        when(message.document()).thenReturn(document);
        when(documentHandler.loadDocument(update)).thenReturn(sendMessage);
        when(telegramBot.execute(sendMessage)).thenReturn(sendResponse);
        when(sendResponse.message().chat().id()).thenReturn(1L);
        when(sendResponse.message().messageId()).thenReturn(1);

        int result = messageProcessor.process(List.of(update));

        assertThat(result).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);
        verify(documentHandler).loadDocument(update);
        verify(documentHandler).saveLastMsg(1L, 1);
    }

    @Test
    void process_ShouldHandleInvalidType() {
        when(update.message()).thenReturn(message);

        int result = messageProcessor.process(List.of(update));

        assertThat(result).isEqualTo(UpdatesListener.CONFIRMED_UPDATES_ALL);
        verify(invalidTypeHandler).handle(update);
    }

}