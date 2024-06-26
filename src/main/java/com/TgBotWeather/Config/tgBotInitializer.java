package com.TgBotWeather.Config;

import com.TgBotWeather.Service.TgBotS;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class tgBotInitializer {
    private final TgBotS tgBotS;

    @Autowired
    public tgBotInitializer(TgBotS tgBotS) {
        this.tgBotS= tgBotS;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        botsApi.registerBot(tgBotS);
    }
}
