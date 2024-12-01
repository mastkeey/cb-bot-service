package org.example.telegrambot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.example.telegrambot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Start implements Command {

    private final static String startMessage = """
        Привет, я телеграм бот, который поможет тебе сохранить свои файлы.
        Для того, чтобы начать мною пользоваться, посмотри команды, которые есть у меня в меню.
        """;

    private final UserService userService;

    @Override
    public String getCommand() {
        return "/start";
    }

    @Override
    public String getDescription() {
        return "Инициализация бота";
    }

    @Override
    public SendMessage handle(Update update) {
        HttpStatus result = userService.createUser(update.message().from().id(), update.message().chat().id(), update.message().from().username());
        return new SendMessage(update.message().chat().id(), result.is2xxSuccessful() ?
            startMessage :
            "Не удалось зарегистрироваться");
    }
}
