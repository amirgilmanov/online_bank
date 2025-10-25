package com.example.online_bank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @SuppressWarnings("checkstyle:RegexpSingleline")
    @Bean
    public OpenAPI openAPI() {
        Server euroBank = new Server();
        euroBank.setUrl("http://localhost:8001");
        euroBank.setDescription("Сервер EuroBank");

        Server moneyBank = new Server();
        moneyBank.setUrl("http://localhost:8002");
        moneyBank.setDescription("Сервер MoneyBank");

        Server defaultBank = new Server();
        defaultBank.setUrl("http://localhost:8080");
        defaultBank.setDescription("Сервер DefaultBank");

        Contact contact = new Contact();
        contact.setEmail("gilmanovamir19@gmail.com");
        contact.setName("Amir Gilmanov");
        contact.setUrl("amirgilmanovofficial.eu");

        Info info = new Info()
                .title("Онлайн-банкинг")
                .version("1.1")
                .contact(contact)
                .description("Api сервис, для работы со счетами клиентов ");
        return new OpenAPI().servers(List.of(euroBank, moneyBank, defaultBank)).info(info);
    }
}
