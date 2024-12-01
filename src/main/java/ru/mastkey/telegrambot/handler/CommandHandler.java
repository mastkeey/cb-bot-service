package ru.mastkey.telegrambot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.mastkey.telegrambot.aop.StructuralLogWithRequestIdFieldAnnotation;
import ru.mastkey.telegrambot.command.Command;
import ru.mastkey.telegrambot.command.workspace.ConnectWorkspace;
import ru.mastkey.telegrambot.command.workspace.CreateWorkspace;
import ru.mastkey.telegrambot.command.workspace.UpdateWorkspace;
import ru.mastkey.telegrambot.enums.InputState;
import ru.mastkey.telegrambot.util.Constants;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@StructuralLogWithRequestIdFieldAnnotation
public class CommandHandler {

    private final List<Command> commands;
    private final Map<Long, InputState> stateMap;
    private final CreateWorkspace createWorkspace;
    private final UpdateWorkspace updateWorkspace;
    private final ConnectWorkspace connectWorkspace;

    public SendMessage handle(Update update) {
        Long userId = update.message().from().id();
        log.info("Received update from user ID: {}", userId);

        if (stateMap.containsKey(userId)) {
            log.info("User ID: {} is in state: {}", userId, stateMap.get(userId));
            return handleState(update);
        }

        log.info("No active state for user ID: {}. Checking commands...", userId);
        Optional<Command> command = commands.stream().filter(e -> e.supports(update)).findFirst();

        if (command.isPresent()) {
            log.info("Command found for user ID: {}: {}", userId, command.get().getClass().getSimpleName());
            return command.get().handle(update);
        } else {
            log.warn("Unknown command received from user ID: {}", userId);
            return new SendMessage(update.message().chat().id(), Constants.UNKNOWN_COMMAND);
        }
    }

    private SendMessage handleState(Update update) {
        Long userId = update.message().from().id();
        InputState state = stateMap.get(userId);

        log.info("Handling state: {} for user ID: {}", state, userId);

        return switch (state) {
            case WAITING_WORKSPACE_CREATE_NAME -> {
                log.info("User ID: {} is creating a workspace", userId);
                yield createWorkspace.create(update);
            }
            case WAITING_WORKSPACE_UPDATE_NAME -> {
                log.info("User ID: {} is updating workspace name", userId);
                yield updateWorkspace.updateName(update);
            }
            case WAITING_WORKSPACE_UUID_TO_CONNECT -> {
                log.info("User ID: {} is connecting to a workspace", userId);
                yield connectWorkspace.connect(update);
            }
        };
    }
}
