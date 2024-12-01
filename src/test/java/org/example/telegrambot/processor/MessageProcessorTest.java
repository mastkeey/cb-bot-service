package org.example.telegrambot.processor;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Document;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.assertj.core.api.Assertions;
import org.example.telegrambot.MessageProcessor;
import org.example.telegrambot.handler.CallbackHandler;
import org.example.telegrambot.handler.CommandHandler;
import org.example.telegrambot.handler.DocumentHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageProcessorTest {

    @Mock
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

    @InjectMocks
    private MessageProcessor messageProcessor;


    @Test
    void process_ShouldCorrectlyWork_WhenTextIsSend() {
        when(update.message()).thenReturn(message);

        int process = messageProcessor.process(List.of(update));

        assertThat(process).isEqualTo(-1);
        verify(commandHandler).handle(update);
    }

    @Test
    void process_ShouldCorrectlyWork_WhenDocumentIsSend() {
        when(update.message()).thenReturn(message);
        when(update.message().document()).thenReturn(document);
        when(documentHandler.loadDocument(update)).thenReturn(sendMessage);
        when(telegramBot.execute(sendMessage)).thenReturn(sendResponse);
        when(sendResponse.message().chat().id()).thenReturn(1L);
        when(sendResponse.message().messageId()).thenReturn(1);

        int process = messageProcessor.process(List.of(update));

        assertThat(process).isEqualTo(-1);
        verify(documentHandler).loadDocument(update);
        verify(documentHandler).saveLastMsg(anyLong(), anyInt());
    }

    @Test
    void process_ShouldCorrectlyWork_WhenCallbackIsSend() {
        when(update.callbackQuery()).thenReturn(callbackQuery);
        when(update.callbackQuery().id()).thenReturn("123");
        when(callbackHandler.handle(callbackQuery)).thenReturn(mock(BaseRequest.class));

        int process = messageProcessor.process(List.of(update));

        assertThat(process).isEqualTo(-1);
        verify(callbackHandler).handle(callbackQuery);
    }
}
