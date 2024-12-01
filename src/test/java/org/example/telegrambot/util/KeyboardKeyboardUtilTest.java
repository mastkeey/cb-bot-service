package org.example.telegrambot.util;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.request.EditMessageReplyMarkup;
import org.example.telegrambot.enums.Action;
import org.example.telegrambot.enums.Type;
import org.example.telegrambot.model.ButtonInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyboardKeyboardUtilTest {

    private UUID first;
    private UUID second;
    private List<ButtonInfo> buttonInfoList;
    private Type type;
    private Action action;

    @BeforeEach
    void setUp() {
        first = UUID.randomUUID();
        second = UUID.randomUUID();
        buttonInfoList = List.of(
            new ButtonInfo("test1", first),
            new ButtonInfo("test2", second)
        );
        type = Type.WORKSPACE;
        action = Action.GET;
    }

    @Test
    void createKeyboard_ShouldReturnKeyboard() {
        int page = 0;
        int lastPage = 1;

        InlineKeyboardButton[][] inlineKeyboardButtons = KeyboardUtil
            .createKeyboard(buttonInfoList, page, lastPage, type, action).inlineKeyboard();

        assertThat(inlineKeyboardButtons[0].length).isEqualTo(2);
        assertThat(inlineKeyboardButtons[0][0].text()).isEqualTo("test1");
        assertThat(inlineKeyboardButtons[0][0].callbackData())
            .isEqualTo(String.join(" ", type.toString(), action.toString(), "test1", first.toString()));
        assertThat(inlineKeyboardButtons[0][1].text()).isEqualTo("test2");
        assertThat(inlineKeyboardButtons[0][1].callbackData())
            .isEqualTo(String.join(" ", type.toString(), action.toString(), "test2", second.toString()));
    }

    @Test
    void createKeyboard_ShouldReturnKeyboardWithZeroArrow() {
        int page = 0;
        int lastPage = 1;

        InlineKeyboardButton[][] inlineKeyboardButtons = KeyboardUtil
            .createKeyboard(buttonInfoList, page, lastPage, type, action).inlineKeyboard();

        assertThat(inlineKeyboardButtons[1].length).isEqualTo(0);
    }

    @Test
    void createKeyboard_ShouldReturnKeyboardWithOnlyRightArrow() {
        int page = 0;
        int lastPage = 2;

        InlineKeyboardButton[][] inlineKeyboardButtons = KeyboardUtil
            .createKeyboard(buttonInfoList, page, lastPage, type, action).inlineKeyboard();

        assertThat(inlineKeyboardButtons[1].length).isEqualTo(1);
        assertThat(inlineKeyboardButtons[1][0].text()).isEqualTo(">");
        assertThat(inlineKeyboardButtons[1][0].callbackData())
            .isEqualTo("right " + (page + 1) + " " + type + " " + action);
    }

    @Test
    void createKeyboard_ShouldReturnKeyboardWithOnlyLeftArrow() {
        int page = 1;
        int lastPage = 2;

        InlineKeyboardButton[][] inlineKeyboardButtons = KeyboardUtil
            .createKeyboard(buttonInfoList, page, lastPage, type, action).inlineKeyboard();

        assertThat(inlineKeyboardButtons[1].length).isEqualTo(1);
        assertThat(inlineKeyboardButtons[1][0].text()).isEqualTo("<");
        assertThat(inlineKeyboardButtons[1][0].callbackData())
            .isEqualTo("left " + (page - 1) + " " + type + " " + action);
    }

    @Test
    void createKeyboard_ShouldReturnKeyboardWithRightAndLeftArrow() {
        int page = 1;
        int lastPage = 3;

        InlineKeyboardButton[][] inlineKeyboardButtons = KeyboardUtil
            .createKeyboard(buttonInfoList, page, lastPage, type, action).inlineKeyboard();

        assertThat(inlineKeyboardButtons[1].length).isEqualTo(2);
        assertThat(inlineKeyboardButtons[1][0].text()).isEqualTo("<");
        assertThat(inlineKeyboardButtons[1][0].callbackData())
            .isEqualTo("left " + (page - 1) + " " + type + " " + action);
        assertThat(inlineKeyboardButtons[1][1].text()).isEqualTo(">");
        assertThat(inlineKeyboardButtons[1][1].callbackData())
            .isEqualTo("right " + (page + 1) + " " + type + " " + action);
    }

    @Test
    void updateKeyboard_ShouldReturnKeyboard() {
        Long chatId = 1L;
        Integer messageId = 1;
        Integer page = 0;
        Integer lastPage = 2;
        String callbackData = "right " + (page + 1) + " " + type + " " + action;

        EditMessageReplyMarkup editMessageReplyMarkup = KeyboardUtil.updateMessage(chatId, messageId, callbackData, buttonInfoList, lastPage);

        assertThat(editMessageReplyMarkup).isNotNull();

    }
}


