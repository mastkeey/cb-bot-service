package org.example.telegrambot.command.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.example.telegrambot.enums.Action;
import org.example.telegrambot.enums.InputState;
import org.example.telegrambot.enums.Type;
import org.example.telegrambot.model.KeyboardInfo;
import org.example.telegrambot.util.KeyboardUtil;
import org.example.telegrambot.service.WorkspaceService;
import org.example.telegrambot.command.Command;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@Qualifier("Function")
@RequiredArgsConstructor
public class UpdateWorkspace implements Command {

    private final WorkspaceService workspaceService;
    private final Map<Long, InputState> stateMap;
    private final Map<Long, UUID> currentWorkspace;

    @Override
    public String getCommand() {
        return "/update";
    }

    @Override
    public String getDescription() {
        return "Обнови название своего воркспейса";
    }

    @Override
    public SendMessage handle(Update update) {
        KeyboardInfo workspaceList = workspaceService.getWorkspaceList(0, 3, update.message().from().id());
        return Objects.isNull(workspaceList) ?
            new SendMessage(update.message().chat().id(), "Не удалось получить информацию") :
            new SendMessage(
            update.message().chat().id(),
            "Выбери рабочее пространство, у которого хочешь изменить название"
        ).replyMarkup(KeyboardUtil.createKeyboard(
            workspaceList.buttonInfoList(), 0, workspaceList.pageTotal(), Type.WORKSPACE, Action.UPDATE
        ));
    }

    public SendMessage updateName(Update update) {
        stateMap.remove(update.message().from().id());
        HttpStatus result = workspaceService.updateWorkspace(currentWorkspace.get(update.message().from().id()), update.message().text());
        if (result.is2xxSuccessful()) {
            currentWorkspace.remove(update.message().from().id());
            return new SendMessage(update.message().chat().id(), "Название успешно изменено");
        } else {
            return new SendMessage(update.message().chat().id(), "Не удалось изменить название");
        }

    }
}

