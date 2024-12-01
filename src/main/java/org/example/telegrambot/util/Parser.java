package org.example.telegrambot.util;

import lombok.experimental.UtilityClass;
import org.example.telegrambot.enums.Action;
import org.example.telegrambot.enums.Type;


import java.util.UUID;

@UtilityClass
public class Parser {

    public Type getButtonType(String string) {
        return Type.valueOf(string.split("/")[0]);
    }

    public Action getButtonAction(String string) {
        return Action.valueOf(string.split("/")[1]);
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
}
