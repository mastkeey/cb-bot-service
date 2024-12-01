package org.example.telegrambot.command.file;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.example.telegrambot.enums.Action;
import org.example.telegrambot.enums.Type;
import org.example.telegrambot.model.KeyboardInfo;
import org.example.telegrambot.service.DocumentService;
import org.example.telegrambot.util.KeyboardUtil;
import org.example.telegrambot.command.Command;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Qualifier("Function")
@RequiredArgsConstructor
public class DownloadFile implements Command {
    private final DocumentService documentService;

    @Override
    public String getCommand() {
        return "/download";
    }

    @Override
    public String getDescription() {
        return "Выбери файл который хочешь получить обратно";
    }

    @Override
    public SendMessage handle(Update update) {
        KeyboardInfo workspaceList = documentService.getFileList(0, 3, update.message().from().id());
        return Objects.isNull(workspaceList) ?
            new SendMessage(update.message().chat().id(), "Не удалось получить список файлов") :
            new SendMessage(update.message().chat().id(), "Выбери файл который хочешь получить")
            .replyMarkup(KeyboardUtil.createKeyboard(
                workspaceList.buttonInfoList(), 0, workspaceList.pageTotal(), Type.FILE, Action.GET)
            );
    }
}
