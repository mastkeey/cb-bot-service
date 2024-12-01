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
@Qualifier("Function")
@RequiredArgsConstructor
public class ChangeWorkspace implements Command {

    private final WorkspaceService workspaceService;

    @Override
    public String getCommand() {
        return "/change";
    }

    @Override
    public String getDescription() {
        return "Выберите рабочее пространство с которым хотите работать";
    }

    @Override
    public SendMessage handle(Update update) {
        KeyboardInfo workspaceList = workspaceService.getWorkspaceList(0, 3, update.message().from().id());
        return Objects.isNull(workspaceList) ?
            new SendMessage(update.message().chat().id(), "Не удалось получить информацию") :
            new SendMessage(update.message().chat().id(), "Выберите рабочее пространство, которое хотите сделать активным")
            .replyMarkup(KeyboardUtil.createKeyboard(
                workspaceList.buttonInfoList(), 0, workspaceList.pageTotal(), Type.WORKSPACE, Action.SELECT)
            );
    }
}
