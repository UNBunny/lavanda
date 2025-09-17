package com.omstu.lavanda.floristic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс Floristic Inventory Service для LAVANDA ERP
 * Специализированный сервис для учета флористических товаров
 */
@SpringBootApplication
public class FloristicInventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FloristicInventoryServiceApplication.class, args);
    }

}
