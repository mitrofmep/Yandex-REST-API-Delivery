package ru.yandex.yandexlavka.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Home redirection to Yandex Lavka api documentation
 */
@Controller
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "redirect:swagger-ui/index.html";
    }

}