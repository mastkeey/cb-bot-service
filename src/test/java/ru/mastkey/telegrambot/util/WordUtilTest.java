package ru.mastkey.telegrambot.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WordUtilTest {

    private String singular = "файл";

    private String pluralFew = "файла";

    private String pluralMany = "файлов";

    @Test
    void getWordByNumber_ShouldReturnSingular() {
        String wordByNumber = WordUtil.getWordByNumber(1, singular, pluralFew, pluralMany);

        assertThat(wordByNumber).isEqualTo(singular);
    }

    @Test
    void getWordByNumber_ShouldReturnPluralFew() {
        String wordByNumber = WordUtil.getWordByNumber(3, singular, pluralFew, pluralMany);

        assertThat(wordByNumber).isEqualTo(pluralFew);
    }

    @Test
    void getWordByNumber_ShouldReturnPluralMany() {
        String wordByNumber = WordUtil.getWordByNumber(7, singular, pluralFew, pluralMany);

        assertThat(wordByNumber).isEqualTo(pluralMany);
    }

    @Test
    void getWordByNumber_ShouldReturnPluralMany_WhenNumberGreaterThanElevenAndLessThanNineteen() {
        String wordByNumber = WordUtil.getWordByNumber(15, singular, pluralFew, pluralMany);

        assertThat(wordByNumber).isEqualTo(pluralMany);
    }
}
