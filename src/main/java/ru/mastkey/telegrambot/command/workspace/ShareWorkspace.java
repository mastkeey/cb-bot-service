package ru.mastkey.telegrambot.command.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import ru.mastkey.telegrambot.command.Command;
import ru.mastkey.telegrambot.service.WorkspaceService;
import ru.mastkey.telegrambot.util.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.mastkey.model.WorkspaceResponse;

import java.util.List;

import static ru.mastkey.telegrambot.util.Constants.*;

@Component
@Qualifier("Function")
@RequiredArgsConstructor
public class ShareWorkspace implements Command {

    private final WorkspaceService workspaceService;

    @Override
    public String getCommand() {
        return SHARE_WORKSPACE_COMMAND;
    }

    @Override
    public String getDescription() {
        return SHARE_WORKSPACE_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        List<WorkspaceResponse> allWorkspace = workspaceService.getAllWorkspace(update.message().from().id());
        String workspaces = allWorkspace.stream()
                .map(workspace -> String.format("%s - ```%s```", workspace.getName(), workspace.getWorkspaceId().toString()))
                .reduce((all, workspace) -> all.concat("\n" + workspace))
                .orElse("");
        return allWorkspace.isEmpty()
                ? new SendMessage(update.message().chat().id(), INFORMATION_NOT_FOUND)
                : new SendMessage(update.message().chat().id(), String.format(SHARE_SUCCESS, workspaces));

    }
}
