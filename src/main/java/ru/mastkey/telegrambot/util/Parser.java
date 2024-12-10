package ru.mastkey.telegrambot.util;

import lombok.experimental.UtilityClass;
import ru.mastkey.telegrambot.enums.Action;
import ru.mastkey.telegrambot.enums.Type;

import java.util.UUID;

@UtilityClass
public class Parser {

    public Action getButtonAction(String string) {
        return Action.valueOf(string.split("/")[0]);
    }

    public String getCallbackAction(String string) {
        return string.split("/")[0];
    }

    public Type getButtonType(String string) {
        return Type.valueOf(string.split("/")[1]);
    }

    public UUID getButtonUUID(String string) {
        return UUID.fromString(string.split("/")[2]);
    }

    public Integer getArrowPageNumber(String string) {
        return Integer.valueOf(string.split("/")[1]);
    }

    public Type getArrowType(String string) {
        return Type.valueOf(string.split("/")[2]);
    }

    public Action getArrowAction(String string) {
        return Action.valueOf(string.split("/")[3]);
    }
}
