package ru.yandex.yandexlavka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)

@ComponentScan(
        basePackages = {"ru.yandex.yandexlavka", "ru.yandex.yandexlavka.controllers", "ru.yandex.yandexlavka.configuration"},
        nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class YandexLavkaApplication {

    public static void main(String[] args) {
        SpringApplication.run(YandexLavkaApplication.class, args);
    }

}
