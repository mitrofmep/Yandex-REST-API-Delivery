package ru.yandex.yandexlavka.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {

    @Bean(name = "ru.yandex.yandexlavka.configuration.SpringDocConfiguration.apiInfo")
    OpenAPI apiInfo() {
        return new OpenAPI().info(new Info().title("Yandex Lavka").description("OpenAPI definition for Yandex Lavka API that allows developers to integrate with the Yandex Lavka service, which provides online grocery shopping and delivery.\n Made by @mitrofmep specially for ШБР-2023").version("1.0"));
    }
}