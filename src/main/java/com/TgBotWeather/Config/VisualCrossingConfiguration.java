package com.TgBotWeather.Config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@Configuration
@PropertySource("application.properties")
public class VisualCrossingConfiguration {

    @Value("${VC_http}")
    private String httpmain;
    @Value("${VC_Token}")
    private String VisualCrossingKey;
}
