package com.omstu.lavanda.floristic.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация OpenAPI/Swagger для флористического учета LAVANDA ERP
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/inventory-service}")
    private String contextPath;

    @Bean
    public OpenAPI floristicInventoryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("LAVANDA ERP - Floristic Inventory Service API")
                        .description("""
                                Специализированный сервис для управления флористическим инвентарем в системе LAVANDA ERP.
                                
                                ## Основные возможности:
                                
                                ### 🌹 Управление цветами
                                - Учет цветов поштучно (розы, тюльпаны, лилии, хризантемы и др.)
                                - Контроль остатков и резервирование
                                - Сезонная доступность и поставщики
                                - Цены и условия хранения
                                
                                ### 🎀 Управление материалами  
                                - Учет материалов по метрам (ленты, упаковочная бумага, проволока)
                                - Контроль остатков и резервирование
                                - Характеристики материалов (цвет, ширина, тип)
                                
                                ### 📅 Календарь свежести
                                - Отслеживание сроков годности цветов по партиям
                                - Автоматические рекомендации по скидкам
                                - Контроль условий хранения (температура, влажность)
                                - Статусы свежести (свежий, хороший, требует скидки, просрочен)
                                
                                ### 📊 Аналитика и отчеты
                                - Статистика по остаткам и продажам
                                - Анализ эффективности поставщиков
                                - Отчеты по свежести и потерям
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("LAVANDA ERP Support")
                                .email("support@lavanda-erp.ru")
                                .url("https://lavanda-erp.ru"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8082" + contextPath)
                                .description("Development Server"),
                        new Server()
                                .url("https://api.lavanda-erp.ru" + contextPath)
                                .description("Production Server")))
                .tags(List.of(
                        new Tag()
                                .name("Flowers")
                                .description("🌹 Управление цветами - CRUD операции, поиск, резервирование, контроль остатков"),
                        new Tag()
                                .name("Materials")
                                .description("🎀 Управление материалами - ленты, упаковка, проволока и другие расходные материалы"),
                        new Tag()
                                .name("Freshness Calendar")
                                .description("📅 Календарь свежести - отслеживание сроков годности, скидки, условия хранения"),
                        new Tag()
                                .name("Statistics")
                                .description("📊 Статистика и аналитика - отчеты по остаткам, продажам, эффективности"),
                        new Tag()
                                .name("Health Check")
                                .description("🏥 Мониторинг состояния сервиса - health checks, метрики, диагностика")
                ));
    }
}
