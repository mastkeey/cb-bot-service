package ru.mastkey.telegrambot.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.mastkey.telegrambot.service.UserService;
import ru.mastkey.telegrambot.util.KeyboardUtil;

import static ru.mastkey.telegrambot.util.Constants.*;

@Component
@RequiredArgsConstructor
public class Start implements Command {

    private final UserService userService;

    @Override
    public String getCommand() {
        return START_COMMAND;
    }

    @Override
    public String getDescription() {
        return START_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        HttpStatus result = userService.createUser(update.message().from().id());
        return result.is2xxSuccessful() ?
            new SendMessage(update.message().chat().id(), HELLO_MESSAGE)
                .replyMarkup(
                    KeyboardUtil.createReplyKeyboard(
                        CREATE_WORKSPACE_COMMAND,
                        CONNECT_WORKSPACE_COMMAND
                    )
                ) :
            new SendMessage(update.message().chat().id(), START_FAILED);
    }
}
