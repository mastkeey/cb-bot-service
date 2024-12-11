package ru.mastkey.telegrambot.command.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.mastkey.telegrambot.command.Command;
import ru.mastkey.telegrambot.enums.Action;
import ru.mastkey.telegrambot.enums.Type;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.WorkspaceService;
import ru.mastkey.telegrambot.util.KeyboardUtil;

import java.util.Objects;

import static ru.mastkey.telegrambot.util.Constants.*;

@Component
@Qualifier("Function")
@RequiredArgsConstructor
public class ChangeWorkspace implements Command {

    private final WorkspaceService workspaceService;

    @Override
    public String getCommand() {
        return CHANGE_WORKSPACE_COMMAND;
    }

    @Override
    public String getDescription() {
        return CHANGE_WORKSPACE_COMMAND_DESCRIPTION;
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
                : new SendMessage(update.message().chat().id(), CHOOSE_WORKSPACE_TO_SELECT)
                .replyMarkup(KeyboardUtil.createKeyboard(
                        workspaceList.buttonInfoList(), DEFAULT_PAGE_NUMBER, workspaceList.pageTotal(), Type.WORKSPACE, Action.SELECT)
                );
    }
}
