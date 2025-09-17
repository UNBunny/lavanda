package com.omstu.lavanda.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация OpenAPI/Swagger для Order Management Service
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8081}")
    private String serverPort;

    @Value("${server.servlet.context-path:/order-service}")
    private String contextPath;

    @Bean
    public OpenAPI orderServiceOpenAPI() {
        Server server = new Server()
                .url("http://localhost:" + serverPort + contextPath)
                .description("Order Management Service - Development");

        Contact contact = new Contact()
                .name("LAVANDA Development Team")
                .email("dev@lavanda.ru")
                .url("https://lavanda.ru");

        License license = new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("LAVANDA Order Management API")
                .version("1.0.0")
                .description("""
                        REST API для управления заказами в цветочной мастерской LAVANDA.
                        
                        ## Основные возможности:
                        - Создание и управление заказами
                        - Отслеживание статусов заказов
                        - Управление позициями заказов
                        - Назначение флористов на заказы
                        - Статистика и аналитика заказов
                        - Поиск заказов по различным критериям
                        
                        ## Статусы заказов:
                        - **NEW** - Новый заказ
                        - **CONFIRMED** - Подтвержден
                        - **IN_PROGRESS** - В работе
                        - **READY** - Готов к выдаче
                        - **OUT_FOR_DELIVERY** - В доставке
                        - **DELIVERED** - Доставлен
                        - **CANCELLED** - Отменен
                        - **RETURNED** - Возвращен
                        
                        ## Специфика флористики:
                        - Учет цветов поштучно (розы, тюльпаны, лилии)
                        - Учет материалов по метрам (ленты, упаковка)
                        - Поддержка букетов и композиций
                        - Календарь свежести товаров
                        """)
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
