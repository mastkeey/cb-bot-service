package ru.mastkey.telegrambot.command.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.mastkey.telegrambot.command.Command;
import ru.mastkey.telegrambot.enums.Action;
import ru.mastkey.telegrambot.enums.InputState;
import ru.mastkey.telegrambot.enums.Type;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.WorkspaceService;
import ru.mastkey.telegrambot.util.KeyboardUtil;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static ru.mastkey.telegrambot.util.Constants.*;

@Component
@Qualifier("Function")
@RequiredArgsConstructor
public class UpdateWorkspace implements Command {

    private final WorkspaceService workspaceService;
    private final Map<Long, InputState> stateMap;
    private final Map<Long, UUID> currentWorkspaceToUpdateName;

    @Override
    public String getCommand() {
        return UPDATE_WORKSPACE_COMMAND;
    }

    @Override
    public String getDescription() {
        return UPDATE_WORKSPACE_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        KeyboardInfo workspaceList = workspaceService.getWorkspaceList(
                update.message().from().id(),
                DEFAULT_PAGE_NUMBER,
                DEFAULT_PAGE_SIZE
        );

        if (Objects.nonNull(workspaceList) && workspaceList.buttonInfoList().isEmpty()) {
            return new SendMessage(update.message().chat().id(), EMPTY_WORKSPACES);
        }

        return Objects.isNull(workspaceList)
                ? new SendMessage(update.message().chat().id(), INFORMATION_NOT_FOUND)
                : new SendMessage(
                update.message().chat().id(),
                CHOOSE_WORKSPACE_TO_UPDATE
        ).replyMarkup(KeyboardUtil.createKeyboard(
                workspaceList.buttonInfoList(), DEFAULT_PAGE_NUMBER, workspaceList.pageTotal(), Type.WORKSPACE, Action.UPDATE
        ));
    }

    public SendMessage updateName(Update update) {
        stateMap.remove(update.message().from().id());

        if (update.message().text().length() > MAX_WORKSPACE_NAME_LENGTH ) {
            return new SendMessage(update.message().chat().id(), WORKSPACE_NAME_TOO_LONG);
        }

        HttpStatus result = workspaceService.updateWorkspace(
                update.message().from().id(),
                currentWorkspaceToUpdateName.get(update.message().from().id()),
                update.message().text()
        );
        if (result.is2xxSuccessful()) {
            currentWorkspaceToUpdateName.remove(update.message().from().id());
            return new SendMessage(update.message().chat().id(), UPDATE_SUCCESS);
        } else {
            return new SendMessage(update.message().chat().id(), UPDATE_FAILED);
        }

    }
}

