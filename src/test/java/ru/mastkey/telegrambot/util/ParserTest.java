package ru.mastkey.telegrambot.util;

import ru.mastkey.telegrambot.enums.Action;
import ru.mastkey.telegrambot.enums.Type;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest {

    private String arrowCallbackData = "right/1/FILE/GET";

    private String buttonCallbackData = "GET/FILE/a0c54917-ecbb-4812-8157-bec12f6b2d96";

    @Test
    void getButtonType_ShouldReturnCorrectType() {
        Type buttonType = Parser.getButtonType(buttonCallbackData);

        assertThat(buttonType).isEqualTo(Type.FILE);
    }

    @Test
    void getButtonAction_ShouldReturnCorrectAction() {
        Action buttonAction = Parser.getButtonAction(buttonCallbackData);

        assertThat(buttonAction).isEqualTo(Action.GET);
    }

    @Test
    void getButtonUUID_ShouldReturnCorrectUUID() {
        UUID buttonUUID = Parser.getButtonUUID(buttonCallbackData);

        assertThat(buttonUUID).isEqualTo(UUID.fromString("a0c54917-ecbb-4812-8157-bec12f6b2d96"));
    }

    @Test
    void getArrowType_ShouldReturnCorrectType() {
        Type arrowType = Parser.getArrowType(arrowCallbackData);

        assertThat(arrowType).isEqualTo(Type.FILE);
    }

    @Test
    void getArrowPageNumber_ShouldReturnCorrectPageNumber() {
        Integer arrowPageNumber = Parser.getArrowPageNumber(arrowCallbackData);

        assertThat(arrowPageNumber).isEqualTo(1);
    }
}
