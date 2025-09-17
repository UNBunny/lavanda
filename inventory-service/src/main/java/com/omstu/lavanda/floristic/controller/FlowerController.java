package com.omstu.lavanda.floristic.controller;

import com.omstu.lavanda.floristic.dto.*;
import com.omstu.lavanda.floristic.mapper.FlowerMapper;
import com.omstu.lavanda.floristic.model.Flower;
import com.omstu.lavanda.floristic.model.FlowerColor;
import com.omstu.lavanda.floristic.model.FlowerType;
import com.omstu.lavanda.floristic.model.SeasonalAvailability;
import com.omstu.lavanda.floristic.service.FlowerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST контроллер для управления цветами в флористическом учете LAVANDA ERP
 */
@RestController
@RequestMapping("/api/floristic/flowers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class FlowerController {

    private final FlowerService flowerService;
    private final FlowerMapper flowerMapper;

    /**
     * Получить все цветы с пагинацией
     */
    @GetMapping
    public ResponseEntity<Page<FlowerDto>> getAllFlowers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("Запрос всех цветов: page={}, size={}, sortBy={}, sortDir={}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Flower> flowers = flowerService.getAllFlowers(pageable);
        Page<FlowerDto> flowerDtos = flowers.map(flowerMapper::toDto);
        
        return ResponseEntity.ok(flowerDtos);
    }

    /**
     * Получить цветок по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<FlowerDto> getFlowerById(@PathVariable Long id) {
        log.info("Запрос цветка по ID: {}", id);
        
        return flowerService.getFlowerById(id)
                .map(flowerMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Создать новый цветок
     */
    @PostMapping
    public ResponseEntity<FlowerDto> createFlower(@Valid @RequestBody CreateFlowerRequest request) {
        log.info("Создание нового цветка: {}", request.getName());
        
        Flower flower = flowerMapper.toEntity(request);
        Flower savedFlower = flowerService.createFlower(flower);
        FlowerDto flowerDto = flowerMapper.toDto(savedFlower);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(flowerDto);
    }

    /**
     * Обновить цветок
     */
    @PutMapping("/{id}")
    public ResponseEntity<FlowerDto> updateFlower(
            @PathVariable Long id,
            @Valid @RequestBody UpdateFlowerRequest request) {
        
        log.info("Обновление цветка ID: {}", id);
        
        return flowerService.getFlowerById(id)
                .map(flower -> {
                    flowerMapper.updateEntity(flower, request);
                    Flower updatedFlower = flowerService.updateFlower(flower);
                    return ResponseEntity.ok(flowerMapper.toDto(updatedFlower));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Удалить цветок
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlower(@PathVariable Long id) {
        log.info("Удаление цветка ID: {}", id);
        
        if (flowerService.getFlowerById(id).isPresent()) {
            flowerService.deleteFlower(id);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }

    /**
     * Поиск цветов по названию
     */
    @GetMapping("/search")
    public ResponseEntity<List<FlowerDto>> searchFlowers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Поиск цветов по запросу: {}", query);
        
        Pageable pageable = PageRequest.of(page, size);
        List<Flower> flowers = flowerService.searchFlowersByName(query, pageable);
        List<FlowerDto> flowerDtos = flowers.stream()
                .map(flowerMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(flowerDtos);
    }

    /**
     * Фильтрация цветов по типу
     */
    @GetMapping("/filter/type/{type}")
    public ResponseEntity<List<FlowerDto>> getFlowersByType(@PathVariable FlowerType type) {
        log.info("Получение цветов по типу: {}", type);
        
        List<Flower> flowers = flowerService.getFlowersByType(type);
        List<FlowerDto> flowerDtos = flowers.stream()
                .map(flowerMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(flowerDtos);
    }

    /**
     * Фильтрация цветов по цвету
     */
    @GetMapping("/filter/color/{color}")
    public ResponseEntity<List<FlowerDto>> getFlowersByColor(@PathVariable FlowerColor color) {
        log.info("Получение цветов по цвету: {}", color);
        
        List<Flower> flowers = flowerService.getFlowersByColor(color);
        List<FlowerDto> flowerDtos = flowers.stream()
                .map(flowerMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(flowerDtos);
    }

    /**
     * Получить цветы в сезоне
     */
    @GetMapping("/in-season")
    public ResponseEntity<List<FlowerDto>> getInSeasonFlowers() {
        log.info("Получение цветов в сезоне");
        
        List<Flower> flowers = flowerService.getInSeasonFlowers();
        List<FlowerDto> flowerDtos = flowers.stream()
                .map(flowerMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(flowerDtos);
    }

    /**
     * Получить цветы с низким остатком
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<FlowerDto>> getLowStockFlowers() {
        log.info("Получение цветов с низким остатком");
        
        List<Flower> flowers = flowerService.getLowStockFlowers();
        List<FlowerDto> flowerDtos = flowers.stream()
                .map(flowerMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(flowerDtos);
    }

    /**
     * Получить истекающие цветы
     */
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<FlowerDto>> getExpiringSoonFlowers() {
        log.info("Получение истекающих цветов");
        
        List<Flower> flowers = flowerService.getExpiringSoonFlowers();
        List<FlowerDto> flowerDtos = flowers.stream()
                .map(flowerMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(flowerDtos);
    }

    /**
     * Обновить остатки цветка
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<FlowerDto> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {
        
        log.info("Обновление остатков цветка ID: {}, количество: {}", id, request.getQuantity());
        
        return flowerService.getFlowerById(id)
                .map(flower -> {
                    Flower updatedFlower = flowerService.updateStock(id, request.getQuantity());
                    return ResponseEntity.ok(flowerMapper.toDto(updatedFlower));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Зарезервировать цветы
     */
    @PostMapping("/{id}/reserve")
    public ResponseEntity<FlowerDto> reserveFlowers(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequest request) {
        
        log.info("Резервирование цветов ID: {}, количество: {}", id, request.getQuantity());
        
        try {
            Flower reservedFlower = flowerService.reserveFlowers(id, request.getQuantity());
            return ResponseEntity.ok(flowerMapper.toDto(reservedFlower));
        } catch (RuntimeException e) {
            log.error("Ошибка резервирования цветов: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Освободить резерв цветов
     */
    @PostMapping("/{id}/release-reserve")
    public ResponseEntity<FlowerDto> releaseReserve(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequest request) {
        
        log.info("Освобождение резерва цветов ID: {}, количество: {}", id, request.getQuantity());
        
        try {
            Flower releasedFlower = flowerService.releaseReserve(id, request.getQuantity());
            return ResponseEntity.ok(flowerMapper.toDto(releasedFlower));
        } catch (RuntimeException e) {
            log.error("Ошибка освобождения резерва: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Проверить доступность цветов
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        
        log.info("Проверка доступности цветка ID: {}, количество: {}", id, quantity);
        
        boolean isAvailable = flowerService.isAvailable(id, quantity);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Получить статистику по цветам
     */
    @GetMapping("/statistics")
    public ResponseEntity<FlowerStatisticsDto> getFlowerStatistics() {
        log.info("Получение статистики по цветам");
        
        List<Object[]> stats = flowerService.getFlowerStatistics();
        
        // Преобразуем статистику в DTO
        FlowerStatisticsDto statisticsDto = FlowerStatisticsDto.builder()
                .totalFlowerTypes((Long) stats.get(0)[0])
                .totalStockQuantity(((Number) stats.get(0)[1]).intValue())
                .totalReservedQuantity(((Number) stats.get(0)[2]).intValue())
                .totalStockValue((BigDecimal) stats.get(0)[3])
                .build();
        
        return ResponseEntity.ok(statisticsDto);
    }

    /**
     * Получить цветы по поставщику
     */
    @GetMapping("/supplier/{supplier}")
    public ResponseEntity<List<FlowerDto>> getFlowersBySupplier(@PathVariable String supplier) {
        log.info("Получение цветов по поставщику: {}", supplier);
        
        List<Flower> flowers = flowerService.getFlowersBySupplier(supplier);
        List<FlowerDto> flowerDtos = flowers.stream()
                .map(flowerMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(flowerDtos);
    }
}
