package org.example.telegrambot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class InvalidTypeHandler {

    public SendMessage handle(Update update) {
        if (Objects.nonNull(update.message().photo()) || Objects.nonNull(update.message().video())) {
            return new SendMessage(
                update.message().chat().id(),
                "Если хотите отправить фотографию или видео, загрузите его как файл"
            );
        } else {
            return new SendMessage(
                update.message().chat().id(),
                "Данный тип сообщения не поддерживается"
            );
        }
    }
}
