package com.TgBotWeather.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("application.properties")
public class BotsConfiguration {
    @Value("${BotName}")
    private String BotName;
    @Value("${BotToken}")
    private String BotToken;
}
