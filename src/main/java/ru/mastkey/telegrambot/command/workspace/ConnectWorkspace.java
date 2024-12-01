package ru.mastkey.telegrambot.command.workspace;


import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import ru.mastkey.telegrambot.command.Command;
import ru.mastkey.telegrambot.enums.InputState;
import ru.mastkey.telegrambot.service.UserService;
import ru.mastkey.telegrambot.util.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static ru.mastkey.telegrambot.util.Constants.*;

@Component
@Qualifier("Function")
@RequiredArgsConstructor
public class ConnectWorkspace implements Command {

    private final Map<Long, InputState> stateMap;
    private final UserService userService;

    @Override
    public String getCommand() {
        return CONNECT_WORKSPACE_COMMAND;
    }

    @Override
    public String getDescription() {
        return CONNECT_WORKSPACE_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        stateMap.put(update.message().from().id(), InputState.WAITING_WORKSPACE_UUID_TO_CONNECT);
        return new SendMessage(update.message().chat().id(), WAITING_WORKSPACE_UUID_INFORMATION);
    }

    public SendMessage connect(Update update) {
        UUID input;

        stateMap.remove(update.message().from().id());
        try {
            input = UUID.fromString(update.message().text());
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.message().chat().id(), INCORRECT_INPUT);
        }

        HttpStatus httpStatus = userService.connectWorkspace(
                update.message().from().id(),
                input
        );
        return httpStatus.is2xxSuccessful()
                ? new SendMessage(update.message().chat().id(), CONNECTION_SUCCESS)
                : new SendMessage(update.message().chat().id(), CONNECTION_FAILED);
    }
}
