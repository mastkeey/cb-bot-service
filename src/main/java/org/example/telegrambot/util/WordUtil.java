package org.example.telegrambot.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WordUtil {

    public String getWordByNumber(int number, String singular, String pluralFew, String pluralMany) {
        int lastDigit = number % 10;
        int lastTwoDigits = number % 100;

        if (lastTwoDigits >= 11 && lastTwoDigits <= 19) {
            return pluralMany;
        }

        return switch (lastDigit) {
            case 1 -> singular;
            case 2, 3, 4 -> pluralFew;
            default -> pluralMany;
        };
    }
}
