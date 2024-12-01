package org.example.telegrambot.command.workspace;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.example.telegrambot.enums.InputState;
import org.example.telegrambot.service.WorkspaceService;
import org.example.telegrambot.command.Command;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Qualifier("Function")
@RequiredArgsConstructor
public class CreateWorkspace implements Command {

    private final WorkspaceService workspaceService;
    private final Map<Long, InputState> stateMap;

    @Override
    public String getCommand() {
        return "/create";
    }

    @Override
    public String getDescription() {
        return "Создай свое рабочее пространство, чтобы сохранить там сообщение";
    }

    @Override
    public SendMessage handle(Update update) {
        stateMap.put(update.message().from().id(), InputState.WAITING_WORKSPACE_CREATE_NAME);
        return new SendMessage(update.message().chat().id(), "Введите название рабочего пространства");
    }

    public SendMessage create(Update update) {
        String msgText = update.message().text();
        Long userId = update.message().from().id();

        stateMap.remove(userId);
        HttpStatus result = workspaceService.createWorkspace(msgText, userId);

        return new SendMessage(update.message().chat().id(),
            result.is2xxSuccessful() ?
                "Рабочее пространство " + msgText + " успешно создано" :
                "Не удалось создать рабочее пространство с названием " + msgText);
    }
}
