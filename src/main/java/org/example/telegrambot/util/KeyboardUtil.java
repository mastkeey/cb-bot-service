package org.example.telegrambot.util;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import lombok.experimental.UtilityClass;
import org.example.telegrambot.enums.Action;
import org.example.telegrambot.enums.Type;
import org.example.telegrambot.model.ButtonInfo;

import java.util.List;

@UtilityClass
public class KeyboardUtil {

    public InlineKeyboardMarkup createKeyboard(List<ButtonInfo> buttonInfoList, int page, int lastPage, Type type, Action action) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        buttonInfoList.stream()
            .map(item -> new InlineKeyboardButton(item.name())
                .callbackData(String.join("/", type.toString(), action.toString(), item.id().toString())))
            .forEach(keyboardMarkup::addRow);
        return keyboardMarkup.addRow(arrowKeyboard(page, lastPage, type, action));
    }

    private InlineKeyboardButton[] arrowKeyboard(int page, int lastPage, Type type, Action action) {
        String rightCallBack = String.join("/", "right", String.valueOf(page + 1), type.toString(), action.toString());
        String leftCallBack = String.join("/", "left", String.valueOf(page - 1), type.toString(), action.toString());
        if (lastPage - 1 == 0) {
            return new InlineKeyboardButton[] {};
        } else if (page == 0) {
            return new InlineKeyboardButton[] { new InlineKeyboardButton(">").callbackData(rightCallBack) };
        } else if (page == lastPage - 1) {
            return new InlineKeyboardButton[] { new InlineKeyboardButton("<").callbackData(leftCallBack) };
        } else {
            return new InlineKeyboardButton[] {
                new InlineKeyboardButton("<").callbackData(leftCallBack),
                new InlineKeyboardButton(">").callbackData(rightCallBack)
            };
        }
    }

    public EditMessageReplyMarkup updateMessage(Long chatId, Integer messageId, String data, List<ButtonInfo> buttonInfoList, int lastPage) {
        EditMessageReplyMarkup editMessageReplyMarkup = new EditMessageReplyMarkup(chatId, messageId);
        String[] split = data.split("/");
        int page = Integer.parseInt(split[1]);
        return editMessageReplyMarkup.replyMarkup(
            createKeyboard(buttonInfoList, page, lastPage, Type.valueOf(split[2]), Action.valueOf(split[3]))
        );
    }
}
