package ru.mastkey.telegrambot.command.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.mastkey.telegrambot.command.Command;
import ru.mastkey.telegrambot.enums.InputState;
import ru.mastkey.telegrambot.service.WorkspaceService;

import java.util.Map;

import static ru.mastkey.telegrambot.util.Constants.*;

@Component
@Qualifier("Function")
@RequiredArgsConstructor
public class CreateWorkspace implements Command {

    private final WorkspaceService workspaceService;
    private final Map<Long, InputState> stateMap;

    @Override
    public String getCommand() {
        return CREATE_WORKSPACE_COMMAND;
    }

    @Override
    public String getDescription() {
        return CREATE_WORKSPACE_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        stateMap.put(update.message().from().id(), InputState.WAITING_WORKSPACE_CREATE_NAME);
        return new SendMessage(update.message().chat().id(), WAITING_WORKSPACE_NAME_TO_CREATE);
    }

    public SendMessage create(Update update) {
        String msgText = update.message().text();

        if (msgText.length() > MAX_WORKSPACE_NAME_LENGTH) {
            return new SendMessage(update.message().chat().id(), WORKSPACE_NAME_TOO_LONG);
        }

        Long userId = update.message().from().id();

        stateMap.remove(userId);
        HttpStatus result = workspaceService.createWorkspace(userId, msgText);

        return new SendMessage(update.message().chat().id(),
                result.is2xxSuccessful()
                        ? String.format(CREATE_SUCCESS, msgText)
                        : String.format(CREATE_FAILED, msgText));
    }
}
