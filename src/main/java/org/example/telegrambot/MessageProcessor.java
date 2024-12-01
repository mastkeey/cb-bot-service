package org.example.telegrambot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.response.SendResponse;
import org.example.telegrambot.handler.CallbackHandler;
import org.example.telegrambot.handler.CommandHandler;
import org.example.telegrambot.handler.DocumentHandler;
import org.example.telegrambot.handler.InvalidTypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class MessageProcessor implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final CommandHandler commandHandler;
    private final CallbackHandler callbackHandler;
    private final DocumentHandler documentHandler;
    private final InvalidTypeHandler invalidTypeHandler;

    @Autowired
    public MessageProcessor(
        TelegramBot telegramBot,
        CommandHandler commandHandler,
        CallbackHandler callbackHandler,
        DocumentHandler documentHandler,
        InvalidTypeHandler invalidTypeHandler
    ) {
        telegramBot.setUpdatesListener(this);

        this.telegramBot = telegramBot;
        this.commandHandler = commandHandler;
        this.callbackHandler = callbackHandler;
        this.documentHandler = documentHandler;
        this.invalidTypeHandler = invalidTypeHandler;
    }

    @Override
    public int process(List<Update> list) {
        list.forEach(update -> {
            if (Objects.nonNull(update)) {
                if (Objects.nonNull(update.message())) {
                    if (Objects.nonNull(update.message().document())) {
                        SendResponse send = telegramBot.execute(documentHandler.loadDocument(update));
                        documentHandler.saveLastMsg(send.message().chat().id(), send.message().messageId());
                    } else if (Objects.nonNull(update.message().voice())) {
                        SendResponse send = telegramBot.execute(documentHandler.loadVoice(update));
                        documentHandler.saveLastMsg(send.message().chat().id(), send.message().messageId());
                    } else if (Objects.nonNull(update.message().videoNote())) {
                        SendResponse send = telegramBot.execute(documentHandler.loadShortVideo(update));
                        documentHandler.saveLastMsg(send.message().chat().id(), send.message().messageId());
                    }
                    else if (Objects.nonNull(update.message().text())) {
                        telegramBot.execute(commandHandler.handle(update));
                    } else {
                        telegramBot.execute(invalidTypeHandler.handle(update));
                    }
                }
                if (Objects.nonNull(update.callbackQuery())) {
                    telegramBot.execute(callbackHandler.handle(update.callbackQuery()));
                    telegramBot.execute(new AnswerCallbackQuery(update.callbackQuery().id()));
                }
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
