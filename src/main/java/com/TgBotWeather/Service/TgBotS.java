package com.TgBotWeather.Service;


import com.TgBotWeather.Config.BotsConfiguration;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class TgBotS extends TelegramLongPollingBot {
@Autowired
private final BotsConfiguration botsConfiguration;
    @Autowired
    public TgBotS(BotsConfiguration botsConfiguration) {
        super(botsConfiguration.getBotToken());
        this.botsConfiguration = botsConfiguration;
    }
    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String chatId = update.getMessage().getChatId().toString();
            sendMultipleMessages(chatId, update.getMessage().getText(), 100);
        }
    }
    private void sendMultipleMessages(String chatId, String text, int count) {
        for (int i = 0; i < count; i++) {

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text + " (message " + (i + 1) + ")");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public String getBotUsername() {
        return botsConfiguration.getBotName();
    }

}
