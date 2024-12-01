package ru.mastkey.telegrambot.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import ru.mastkey.telegrambot.command.Command;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommandTest {

    private Command command;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Update update;

    @BeforeEach
    void setUp() {
        command = new Command() {
            @Override
            public String getCommand() {
                return "/command";
            }

            @Override
            public String getDescription() {
                return "Обычная команда";
            }

            @Override
            public SendMessage handle(Update update) {
                return null;
            }
        };
    }

    @Test
    void commandSupportsTest_ShouldReturnTrue() {
        when(update.message().text()).thenReturn("/command");

        boolean supports = command.supports(update);

        assertThat(supports).isTrue();
    }

    @Test
    void commandSupportsTest_ShouldReturnFalse() {
        when(update.message().text()).thenReturn("/wrong");

        boolean supports = command.supports(update);

        assertThat(supports).isFalse();
    }

    @Test
    void commandSupportsTest_ShouldReturnFalse_WhenTextIsNull() {
        boolean supports = command.supports(update);

        assertThat(supports).isFalse();
    }

    @Test
    void commandToMenu_ShouldReturnCorrectCommandAndDescription() {
        BotCommand menu = command.toMenu();

        assertThat(menu.command()).isEqualTo("/command");
        assertThat(menu.description()).isEqualTo("Обычная команда");
    }
}
