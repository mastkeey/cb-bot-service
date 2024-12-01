package org.example.telegrambot.command.file;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.example.telegrambot.command.Command;
import org.example.telegrambot.enums.Action;
import org.example.telegrambot.enums.Type;
import org.example.telegrambot.model.KeyboardInfo;
import org.example.telegrambot.service.DocumentService;
import org.example.telegrambot.util.KeyboardUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Qualifier("Function")
@RequiredArgsConstructor
public class DeleteFile implements Command {
    private final DocumentService documentService;

    @Override
    public String getCommand() {
        return "/delete_file";
    }

    @Override
    public String getDescription() {
        return "Выбери файл который хочешь удалить";
    }

    @Override
    public SendMessage handle(Update update) {
        KeyboardInfo documentList = documentService.getFileList(0, 3, update.message().from().id());
        return Objects.isNull(documentList) ?
            new SendMessage(update.message().chat().id(), "Не удалось получить информацию") :
            new SendMessage(update.message().chat().id(), "Выбери файл который хочешь удалить")
            .replyMarkup(KeyboardUtil.createKeyboard(
                documentList.buttonInfoList(), 0, documentList.pageTotal(), Type.FILE, Action.DELETE)
            );
    }
}
