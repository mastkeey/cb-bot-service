package org.example.telegrambot.command.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.example.telegrambot.enums.Action;
import org.example.telegrambot.enums.Type;
import org.example.telegrambot.model.KeyboardInfo;
import org.example.telegrambot.util.KeyboardUtil;
import org.example.telegrambot.service.WorkspaceService;
import org.example.telegrambot.command.Command;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Qualifier("Function")
public class DeleteWorkspace implements Command {
    private final WorkspaceService workspaceService;

    @Override
    public String getCommand() {
        return "/delete_workspace";
    }

    @Override
    public String getDescription() {
        return "Выбери рабочее пространство, которое хочешь удалить";
    }

    @Override
    public SendMessage handle(Update update) {
        KeyboardInfo workspaceList = workspaceService.getWorkspaceList(0, 3, update.message().from().id());
        return Objects.isNull(workspaceList) ?
            new SendMessage(update.message().chat().id(), "Не удалось получить информацию") :
            new SendMessage(update.message().chat().id(), "Выбери рабочее пространство, которе хочешь удалить")
            .replyMarkup(KeyboardUtil.createKeyboard(
                workspaceList.buttonInfoList(), 0, workspaceList.pageTotal(), Type.WORKSPACE, Action.DELETE
            ));
    }
}
