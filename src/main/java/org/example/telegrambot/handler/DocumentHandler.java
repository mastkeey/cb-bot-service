package org.example.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.example.telegrambot.service.DocumentService;
import org.example.telegrambot.util.WordUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DocumentHandler {

    private final Map<Long, Integer> latestMsg;
    private final DocumentService documentService;
    private final TelegramBot telegramBot;

    public SendMessage loadDocument(Update update) {
        deleteLastMsg(update.message().from().id());
        return processFile(
            update.message().document().fileId(),
            update.message().document().fileName(),
            update.message().from().id(),
            update.message().chat().id(),
            "Не удалось прикрепить файл",
            "Вы прикрепили %d %s",
            " файл\n", " файла\n", " файлов\n",
            "send"
        );
    }

    public SendMessage loadVoice(Update update) {
        String fileName = generateFileName("VM", "mp3");
        return processFile(
            update.message().voice().fileId(),
            fileName,
            update.message().from().id(),
            update.message().chat().id(),
            "Не удалось прикрепить голосовое сообщение",
            "Вы прикрепили %d %s",
            " файл\n", " файла\n", " файлов\n",
            "send"
        );
    }

    public SendMessage loadShortVideo(Update update) {
        String fileName = generateFileName("CV", "mp4");
        return processFile(
            update.message().videoNote().fileId(),
            fileName,
            update.message().from().id(),
            update.message().chat().id(),
            "Не удалось прикрепить видео сообщение",
            "Вы прикрепили %d %s",
            " файл\n", " файла\n", " файлов\n",
            "send"
        );
    }

    private SendMessage processFile(
        String fileId,
        String fileName,
        Long userId,
        Long chatId,
        String failureMessage,
        String successMessageTemplate,
        String single, String few, String many,
        String callbackData
    ) {
        GetFile getFile = new GetFile(fileId);
        File file = telegramBot.execute(getFile).file();

        List<String> names = documentService.addFile(userId, fileName, file);

        if (names.isEmpty()) {
            return new SendMessage(chatId, failureMessage);
        }

        String wordForm = WordUtil.getWordByNumber(names.size(), single, few, many);
        String message = String.format(successMessageTemplate, names.size(), wordForm);
        String fileNamesList = names.stream().reduce((all, name) -> all.concat("\n" + name)).orElse("");
        message = message.concat(fileNamesList);

        return new SendMessage(chatId, message)
            .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Отправить").callbackData(callbackData)));
    }

    public void deleteLastMsg(Long userId) {
        if (latestMsg.containsKey(userId)) {
            telegramBot.execute(new DeleteMessage(userId, latestMsg.get(userId)));
        }
    }

    private String generateFileName(String prefix, String extension) {
        return String.format("%s_%s.%s",
            prefix,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_hh:mm:ss")),
            extension
        );
    }

    public void saveLastMsg(Long userId, Integer messageId) {
        latestMsg.put(userId, messageId);
    }
}
