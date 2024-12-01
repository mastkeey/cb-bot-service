package org.example.telegrambot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.example.telegrambot.command.Command;
import org.example.telegrambot.command.workspace.CreateWorkspace;
import org.example.telegrambot.command.workspace.UpdateWorkspace;
import org.example.telegrambot.enums.InputState;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CommandHandler {

    private final List<Command> commands;
    private final Map<Long, InputState> stateMap;
    private final CreateWorkspace createWorkspace;
    private final UpdateWorkspace updateWorkspace;


    public SendMessage handle(Update update) {
        if (stateMap.containsKey(update.message().from().id())) {
            return handleState(update);
        }
        Optional<Command> command = commands.stream().filter(e -> e.supports(update)).findFirst();
        return command.isPresent()
            ? command.get().handle(update)
            : new SendMessage(update.message().chat().id(), "Команда не найдена");
    }

    private SendMessage handleState(Update update){
        if (Objects.equals(stateMap.get(update.message().from().id()), InputState.WAITING_WORKSPACE_CREATE_NAME)) {
            return createWorkspace.create(update);
        } else {
            return updateWorkspace.updateName(update);
        }
    }
}
