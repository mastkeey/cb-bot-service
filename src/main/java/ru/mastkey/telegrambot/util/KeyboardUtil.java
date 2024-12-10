package ru.mastkey.telegrambot.util;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import lombok.experimental.UtilityClass;
import ru.mastkey.telegrambot.enums.Action;
import ru.mastkey.telegrambot.enums.Type;
import ru.mastkey.telegrambot.model.ButtonInfo;

import java.util.Arrays;
import java.util.List;

@UtilityClass
public class KeyboardUtil {

    public ReplyKeyboardMarkup createReplyKeyboard(String... buttonCommands) {
        return new ReplyKeyboardMarkup(Arrays.stream(buttonCommands)
                .map(KeyboardButton::new)
                .toArray(KeyboardButton[]::new)).oneTimeKeyboard(true);
    }

    public InlineKeyboardMarkup createKeyboard(List<ButtonInfo> buttonInfoList, int page, int lastPage, Type type, Action action) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        buttonInfoList.stream()
                .map(item -> new InlineKeyboardButton(item.name())
                        .callbackData(String.join("/", action.toString(), type.toString(), item.id().toString())))
                .forEach(keyboardMarkup::addRow);
        return keyboardMarkup.addRow(arrowKeyboard(page, lastPage, type, action));
    }

    private InlineKeyboardButton[] arrowKeyboard(int page, int lastPage, Type type, Action action) {
        String rightCallBack = String.join("/", "right", String.valueOf(page + 1), type.toString(), action.toString());
        String leftCallBack = String.join("/", "left", String.valueOf(page - 1), type.toString(), action.toString());
        if (lastPage - 1 == 0) {
            return new InlineKeyboardButton[]{};
        } else if (page == 0) {
            return new InlineKeyboardButton[]{new InlineKeyboardButton(">").callbackData(rightCallBack)};
        } else if (page == lastPage - 1) {
            return new InlineKeyboardButton[]{new InlineKeyboardButton("<").callbackData(leftCallBack)};
        } else {
            return new InlineKeyboardButton[]{
                    new InlineKeyboardButton("<").callbackData(leftCallBack),
                    new InlineKeyboardButton(">").callbackData(rightCallBack)
            };
        }
    }

    public EditMessageReplyMarkup updateMessage(Long chatId, Integer messageId, String data, List<ButtonInfo> buttonInfoList, int lastPage) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(chatId, messageId);
        int page = Parser.getArrowPageNumber(data);
        return editMessageReplyMarkup.replyMarkup(
                createKeyboard(buttonInfoList, page, lastPage, Parser.getArrowType(data), Parser.getArrowAction(data))
        );
    }
}
