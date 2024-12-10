package ru.mastkey.telegrambot.handler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.mastkey.telegrambot.aop.StructuralLogWithRequestIdFieldAnnotation;
import ru.mastkey.telegrambot.service.DocumentService;
import ru.mastkey.telegrambot.util.WordUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static ru.mastkey.telegrambot.util.Constants.*;

@Slf4j
@Component
@RequiredArgsConstructor
@StructuralLogWithRequestIdFieldAnnotation
public class DocumentHandler {

    private final Map<Long, Integer> latestMsg;
    private final DocumentService documentService;
    private final TelegramBot telegramBot;

    public SendMessage loadDocument(Update update) {
        Long userId = update.message().from().id();
        log.info("Loading document for user ID: {}", userId);

        if (update.message().document().fileName().length() > MAX_FILE_NAME_LENGTH) {
            return new SendMessage(update.message().chat().id(), FILE_NAME_TOO_LONG);
        }

        return update.message().document().fileSize() > DEFAULT_FILE_MAX_SIZE
                ? new SendMessage(update.message().chat().id(), FILE_TOO_LARGE)
                : processFile(
                update.message().document().fileId(),
                update.message().document().fileName(),
                userId,
                update.message().chat().id(),
                LOAD_DOCUMENT_FAILED
        );
    }

    public SendMessage loadVoice(Update update) {
        Long userId = update.message().from().id();
        log.info("Loading voice message for user ID: {}", userId);

        String fileName = generateFileName("VM", "mp3");
        return update.message().voice().fileSize() > DEFAULT_FILE_MAX_SIZE
                ? new SendMessage(update.message().chat().id(), FILE_TOO_LARGE)
                : processFile(
                update.message().voice().fileId(),
                fileName,
                userId,
                update.message().chat().id(),
                LOAD_VOICE_MESSAGE_FAILED
        );
    }

    public SendMessage loadShortVideo(Update update) {
        Long userId = update.message().from().id();
        log.info("Loading short video for user ID: {}", userId);

        String fileName = generateFileName("CV", "mp4");

        return update.message().videoNote().fileSize() > DEFAULT_FILE_MAX_SIZE
                ? new SendMessage(update.message().chat().id(), FILE_TOO_LARGE)
                : processFile(
                update.message().videoNote().fileId(),
                fileName,
                userId,
                update.message().chat().id(),
                LOAD_VIDEO_MESSAGE_FAILED
        );
    }

    private SendMessage processFile(
            String fileId,
            String fileName,
            Long userId,
            Long chatId,
            String failureMessage
    ) {
        deleteLastMsg(userId);

        log.info("Processing file for user ID: {} with file name: {}", userId, fileName);

        GetFile getFile = new GetFile(fileId);
        File file = telegramBot.execute(getFile).file();

        log.info("File size is within the limit for user ID: {}. Adding file to document service...", userId);
        List<String> names = documentService.addFile(userId, fileName, file);

        if (names.isEmpty()) {
            log.error("Failed to upload file for user ID: {}. File name: {}", userId, fileName);
            return new SendMessage(chatId, failureMessage);
        }

        log.info("Successfully uploaded file for user ID: {}. Total files uploaded: {}", userId, names.size());
        String wordForm = WordUtil.getWordByNumber(names.size(), FILE_SINGULAR, FILE_PLURAL_FEW, FILE_PLURAL_MANY);
        String message = String.format(FILE_UPLOAD_TEMPLATE, names.size(), wordForm);
        if (names.size() == DEFAULT_FILE_MAX_COUNT) {
            message = MAX_FILE_COUNT_MESSAGE.concat(message);
        }
        String fileNamesList = names.stream().reduce((all, name) -> all.concat("\n" + name)).get();
        message = message.concat(fileNamesList);

        log.info("Generated response message for user ID: {}", userId);
        return new SendMessage(chatId, message)
                .replyMarkup(new InlineKeyboardMarkup(new InlineKeyboardButton("Отправить").callbackData("send")));
    }

    public void deleteLastMsg(Long userId) {
        if (latestMsg.containsKey(userId)) {
            Integer messageId = latestMsg.get(userId);
            log.info("Deleting last message for user ID: {} with message ID: {}", userId, messageId);
            telegramBot.execute(new DeleteMessage(userId, messageId));
        } else {
            log.warn("No last message found for user ID: {}", userId);
        }
    }

    public String generateFileName(String prefix, String extension) {
        String fileName = String.format("%s_%s.%s",
                prefix,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_hh:mm:ss")),
                extension
        );
        log.info("Generated file name: {}", fileName);
        return fileName;
    }

    public void saveLastMsg(Long userId, Integer messageId) {
        log.info("Saving last message ID: {} for user ID: {}", messageId, userId);
        latestMsg.put(userId, messageId);
    }
}
