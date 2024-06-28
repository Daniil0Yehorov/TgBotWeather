package com.TgBotWeather.Service;


import com.TgBotWeather.Config.BotsConfiguration;
import com.TgBotWeather.Config.VisualCrossingConfiguration;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

@Service
public class TgBotS extends TelegramLongPollingBot {

    @Autowired
    private VisualCrossingConfiguration visualCrossingConfiguration;

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
            String text = update.getMessage().getText();

            switch (text){
                case "/info":
                    sendMessage(chatId, "This bot gives u information about weather in your country." +
                            "Comands: /weather <Countryname> <Startdate in format YYYY-MM-DD> <EndDate>");
                    break;
                case "/start":
                    startCommand(chatId, update.getMessage().getChat().getFirstName());
                    break;

                default:
                    if (text.startsWith("/weather")) {
                        String[] parts = text.split(" ");
                        if (parts.length == 4) {
                            String country = parts[1];
                            String startDate = parts[2];
                            String endDate = parts[3];
                            sendWeather(chatId, country, startDate, endDate);
                        } else {
                            sendMessage(chatId, "Please provide the country and date range in the format: /weather <country> <startDate::YYYY-MM-DD> <endDate:YYYY-MM-DD>");
                        }
                    } else {
                        sendMessage(chatId, "Unknown command.Comand for info: /start -> /info");
                    }
            }

        }
    }
    private void startCommand(String chatId, String firstName) {
        String answer = firstName + ", hello my friend. Nice to meet you. I can help u to find" +
                " information about weather in ur country. Write comand /info";
        sendMessage(chatId, answer);
    }
    private void sendWeather(String chatId, String country, String startDate, String endDate) {
        String weatherInfo = getWeather(country, startDate, endDate);
        sendMessage(chatId, weatherInfo);
    }

    private void sendMessage(String chatid,String text){
        SendMessage message=new SendMessage();
        message.setChatId(chatid);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String getWeather(String country, String startDate, String endDate) {
        String url = visualCrossingConfiguration.getHttpmain() + country + "/" + startDate + "/" + endDate
                + "?key=" + visualCrossingConfiguration.getVisualCrossingKey();
        HttpGet request = new HttpGet(url);

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)) {

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String result = EntityUtils.toString(entity);
                JSONObject json = new JSONObject(result);

                // Parsing the weather data from the JSON response
                JSONArray days = json.getJSONArray("days");
                StringBuilder weatherInfo = new StringBuilder("Weather forecast:\n");

                for (int i = 0; i < days.length(); i++) {
                    JSONObject day = days.getJSONObject(i);
                    String date = day.getString("datetime");
                    double tempMax = day.getDouble("tempmax");
                    double tempMin = day.getDouble("tempmin");
                    String conditions = day.getString("conditions");
                    //Temperature in fahrenheit
                    weatherInfo.append(String.format("Date: %s\nMax Temp: %.1f°C\nMin Temp: %.1f°C\nConditions: %s\n\n",
                            date, tempMax, tempMin, conditions));
                }

                return weatherInfo.toString();
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return "Failed to retrieve weather data";
    }


    @Override
    public String getBotUsername() {
        return botsConfiguration.getBotName();
    }

}
