package org.example.telegrambot.config;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.example.telegrambot.command.Command;
import org.example.telegrambot.config.properties.TokenProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BotConfiguration {

    @Bean
    public TelegramBot telegramBot(TokenProperties tokenProperties, @Qualifier("Function") List<Command> commands) {
        TelegramBot telegramBot = new TelegramBot(tokenProperties.getToken());
        createMenu(telegramBot, commands);
        return telegramBot;
    }

    public void createMenu(TelegramBot telegramBot, List<Command> commands) {
        telegramBot.execute(
            new SetMyCommands(
                commands.stream()
                    .map(Command::toMenu)
                    .toArray(BotCommand[]::new)
            )
        );
    }
}
