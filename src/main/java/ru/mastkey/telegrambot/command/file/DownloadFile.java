package ru.mastkey.telegrambot.command.file;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.mastkey.telegrambot.command.Command;
import ru.mastkey.telegrambot.enums.Action;
import ru.mastkey.telegrambot.enums.Type;
import ru.mastkey.telegrambot.model.KeyboardInfo;
import ru.mastkey.telegrambot.service.DocumentService;
import ru.mastkey.telegrambot.util.KeyboardUtil;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static ru.mastkey.telegrambot.util.Constants.*;

@Component
@Qualifier("Function")
@RequiredArgsConstructor
public class DownloadFile implements Command {
    private final DocumentService documentService;
    private final Map<Long, UUID> currentWorkspace;

    @Override
    public String getCommand() {
        return DOWNLOAD_FILE_COMMAND;
    }

    @Override
    public String getDescription() {
        return DOWNLOAD_FILE_COMMAND_DESCRIPTION;
    }

    @Override
    public SendMessage handle(Update update) {
        KeyboardInfo documentList = documentService.getFileList(
                update.message().from().id(),
                currentWorkspace.get(update.message().from().id()),
                DEFAULT_PAGE_NUMBER,
                DEFAULT_PAGE_SIZE
        );

        if (Objects.nonNull(documentList) && documentList.buttonInfoList().isEmpty()) {
            return new SendMessage(update.message().chat().id(), EMPTY_FILES);
        }

        return Objects.isNull(documentList)
                ? new SendMessage(update.message().chat().id(), FILE_LIST_NOT_FOUND)
                : new SendMessage(update.message().chat().id(), CHOOSE_FILE_TO_GET)
                .replyMarkup(KeyboardUtil.createKeyboard(
                        documentList.buttonInfoList(), DEFAULT_PAGE_NUMBER, documentList.pageTotal(), Type.FILE, Action.GET)
                );
    }
}
