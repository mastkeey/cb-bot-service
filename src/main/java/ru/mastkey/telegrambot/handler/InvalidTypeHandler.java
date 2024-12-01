package ru.mastkey.telegrambot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import ru.mastkey.telegrambot.aop.StructuralLogWithRequestIdFieldAnnotation;
import ru.mastkey.telegrambot.util.Constants;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
@StructuralLogWithRequestIdFieldAnnotation
public class InvalidTypeHandler {

    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        log.info("Handling invalid message type for chat ID: {}", chatId);
        if (Objects.nonNull(update.message().photo()) || Objects.nonNull(update.message().video())) {
            log.warn("Received invalid photo or video message in chat ID: {}", chatId);
            return new SendMessage(
                update.message().chat().id(),
                Constants.INVALID_PHOTO_OR_VIDEO
            );
        } else {
            log.warn("Received message with unsupported type in chat ID: {}", chatId);
            return new SendMessage(
                update.message().chat().id(),
                Constants.INVALID_TYPE
            );
        }
    }
}
