package com.omstu.lavanda.floristic.controller;

import com.omstu.lavanda.floristic.dto.*;
import com.omstu.lavanda.floristic.mapper.MaterialMapper;
import com.omstu.lavanda.floristic.model.Material;
import com.omstu.lavanda.floristic.model.MaterialType;
import com.omstu.lavanda.floristic.service.MaterialService;
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
 * REST контроллер для управления материалами в флористическом учете LAVANDA ERP
 */
@RestController
@RequestMapping("/api/floristic/materials")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class MaterialController {

    private final MaterialService materialService;
    private final MaterialMapper materialMapper;

    /**
     * Получить все материалы с пагинацией
     */
    @GetMapping
    public ResponseEntity<Page<MaterialDto>> getAllMaterials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        log.info("Запрос всех материалов: page={}, size={}, sortBy={}, sortDir={}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Material> materials = materialService.getAllMaterials(pageable);
        Page<MaterialDto> materialDtos = materials.map(materialMapper::toDto);
        
        return ResponseEntity.ok(materialDtos);
    }

    /**
     * Получить материал по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MaterialDto> getMaterialById(@PathVariable Long id) {
        log.info("Запрос материала по ID: {}", id);
        
        return materialService.getMaterialById(id)
                .map(materialMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Создать новый материал
     */
    @PostMapping
    public ResponseEntity<MaterialDto> createMaterial(@Valid @RequestBody CreateMaterialRequest request) {
        log.info("Создание нового материала: {}", request.getName());
        
        Material material = materialMapper.toEntity(request);
        Material savedMaterial = materialService.createMaterial(material);
        MaterialDto materialDto = materialMapper.toDto(savedMaterial);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(materialDto);
    }

    /**
     * Обновить материал
     */
    @PutMapping("/{id}")
    public ResponseEntity<MaterialDto> updateMaterial(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMaterialRequest request) {
        
        log.info("Обновление материала ID: {}", id);
        
        return materialService.getMaterialById(id)
                .map(material -> {
                    materialMapper.updateEntity(material, request);
                    Material updatedMaterial = materialService.updateMaterial(material);
                    return ResponseEntity.ok(materialMapper.toDto(updatedMaterial));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Удалить материал
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        log.info("Удаление материала ID: {}", id);
        
        if (materialService.getMaterialById(id).isPresent()) {
            materialService.deleteMaterial(id);
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }

    /**
     * Поиск материалов по названию
     */
    @GetMapping("/search")
    public ResponseEntity<List<MaterialDto>> searchMaterials(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        log.info("Поиск материалов по запросу: {}", query);
        
        Pageable pageable = PageRequest.of(page, size);
        List<Material> materials = materialService.searchMaterialsByName(query, pageable);
        List<MaterialDto> materialDtos = materials.stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(materialDtos);
    }

    /**
     * Фильтрация материалов по типу
     */
    @GetMapping("/filter/type/{type}")
    public ResponseEntity<List<MaterialDto>> getMaterialsByType(@PathVariable MaterialType type) {
        log.info("Получение материалов по типу: {}", type);
        
        List<Material> materials = materialService.getMaterialsByType(type);
        List<MaterialDto> materialDtos = materials.stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(materialDtos);
    }

    /**
     * Получить материалы с низким остатком
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<MaterialDto>> getLowStockMaterials() {
        log.info("Получение материалов с низким остатком");
        
        List<Material> materials = materialService.getLowStockMaterials();
        List<MaterialDto> materialDtos = materials.stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(materialDtos);
    }

    /**
     * Обновить остатки материала
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<MaterialDto> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request) {
        
        log.info("Обновление остатков материала ID: {}, метры: {}", id, request.getMeters());
        
        return materialService.getMaterialById(id)
                .map(material -> {
                    Material updatedMaterial = materialService.updateStock(id, request.getMeters());
                    return ResponseEntity.ok(materialMapper.toDto(updatedMaterial));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Зарезервировать материал
     */
    @PostMapping("/{id}/reserve")
    public ResponseEntity<MaterialDto> reserveMaterial(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequest request) {
        
        log.info("Резервирование материала ID: {}, метры: {}", id, request.getMeters());
        
        try {
            Material reservedMaterial = materialService.reserveMaterial(id, request.getMeters());
            return ResponseEntity.ok(materialMapper.toDto(reservedMaterial));
        } catch (RuntimeException e) {
            log.error("Ошибка резервирования материала: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Освободить резерв материала
     */
    @PostMapping("/{id}/release-reserve")
    public ResponseEntity<MaterialDto> releaseReserve(
            @PathVariable Long id,
            @Valid @RequestBody ReservationRequest request) {
        
        log.info("Освобождение резерва материала ID: {}, метры: {}", id, request.getMeters());
        
        try {
            Material releasedMaterial = materialService.releaseReserve(id, request.getMeters());
            return ResponseEntity.ok(materialMapper.toDto(releasedMaterial));
        } catch (RuntimeException e) {
            log.error("Ошибка освобождения резерва: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Проверить доступность материала
     */
    @GetMapping("/{id}/availability")
    public ResponseEntity<Boolean> checkAvailability(
            @PathVariable Long id,
            @RequestParam BigDecimal meters) {
        
        log.info("Проверка доступности материала ID: {}, метры: {}", id, meters);
        
        boolean isAvailable = materialService.isAvailable(id, meters);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Получить статистику по материалам
     */
    @GetMapping("/statistics")
    public ResponseEntity<MaterialStatisticsDto> getMaterialStatistics() {
        log.info("Получение статистики по материалам");
        
        List<Object[]> stats = materialService.getMaterialStatistics();
        
        // Преобразуем статистику в DTO
        MaterialStatisticsDto statisticsDto = MaterialStatisticsDto.builder()
                .totalMaterialTypes((Long) stats.get(0)[0])
                .totalStockMeters((BigDecimal) stats.get(0)[1])
                .totalReservedMeters((BigDecimal) stats.get(0)[2])
                .totalStockValue((BigDecimal) stats.get(0)[3])
                .build();
        
        return ResponseEntity.ok(statisticsDto);
    }

    /**
     * Получить материалы по поставщику
     */
    @GetMapping("/supplier/{supplier}")
    public ResponseEntity<List<MaterialDto>> getMaterialsBySupplier(@PathVariable String supplier) {
        log.info("Получение материалов по поставщику: {}", supplier);
        
        List<Material> materials = materialService.getMaterialsBySupplier(supplier);
        List<MaterialDto> materialDtos = materials.stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(materialDtos);
    }

    /**
     * Получить материалы по цвету
     */
    @GetMapping("/filter/color/{color}")
    public ResponseEntity<List<MaterialDto>> getMaterialsByColor(@PathVariable String color) {
        log.info("Получение материалов по цвету: {}", color);
        
        List<Material> materials = materialService.getMaterialsByColor(color);
        List<MaterialDto> materialDtos = materials.stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(materialDtos);
    }

    /**
     * Получить материалы по ширине
     */
    @GetMapping("/filter/width/{width}")
    public ResponseEntity<List<MaterialDto>> getMaterialsByWidth(@PathVariable String width) {
        log.info("Получение материалов по ширине: {}", width);
        
        List<Material> materials = materialService.getMaterialsByWidth(width);
        List<MaterialDto> materialDtos = materials.stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(materialDtos);
    }

    /**
     * Получить материалы в ценовом диапазоне
     */
    @GetMapping("/filter/price-range")
    public ResponseEntity<List<MaterialDto>> getMaterialsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        
        log.info("Получение материалов в ценовом диапазоне: {} - {}", minPrice, maxPrice);
        
        List<Material> materials = materialService.getMaterialsByPriceRange(minPrice, maxPrice);
        List<MaterialDto> materialDtos = materials.stream()
                .map(materialMapper::toDto)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(materialDtos);
    }
}
