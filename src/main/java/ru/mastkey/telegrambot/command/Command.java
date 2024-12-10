package ru.mastkey.telegrambot.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {

    String getCommand();

    String getDescription();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        if (update.message().text() != null) {
            return update.message().text().contains(getCommand());
        }
        return false;
    }

    default BotCommand toMenu() {
        return new BotCommand(getCommand(), getDescription());
    }
}
