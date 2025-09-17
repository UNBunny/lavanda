package com.omstu.lavanda.order.controller;

import com.omstu.lavanda.order.dto.*;
import com.omstu.lavanda.order.mapper.OrderMapper;
import com.omstu.lavanda.order.model.Order;
import com.omstu.lavanda.order.model.OrderStatus;
import com.omstu.lavanda.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST контроллер для управления заказами в LAVANDA ERP
 */
@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "API для управления заказами в цветочной мастерской LAVANDA")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @Operation(summary = "Создать новый заказ", description = "Создает новый заказ в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Заказ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные заказа"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        log.info("Создание нового заказа для клиента: {}", request.getCustomerName());
        
        Order order = orderMapper.toEntity(request);
        Order createdOrder = orderService.createOrder(order);
        OrderDto orderDto = orderMapper.toDto(createdOrder);
        
        log.info("Заказ создан с номером: {}", createdOrder.getOrderNumber());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
    }

    @Operation(summary = "Получить заказ по ID", description = "Возвращает заказ по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказ найден"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(
            @Parameter(description = "ID заказа") @PathVariable Long id) {
        log.debug("Получение заказа по ID: {}", id);
        
        return orderService.getOrderById(id)
                .map(orderMapper::toDto)
                .map(orderDto -> ResponseEntity.ok().body(orderDto))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить заказ по номеру", description = "Возвращает заказ по его номеру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказ найден"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден")
    })
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderDto> getOrderByNumber(
            @Parameter(description = "Номер заказа") @PathVariable String orderNumber) {
        log.debug("Получение заказа по номеру: {}", orderNumber);
        
        return orderService.getOrderByNumber(orderNumber)
                .map(orderMapper::toDto)
                .map(orderDto -> ResponseEntity.ok().body(orderDto))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить все заказы", description = "Возвращает список всех заказов с пагинацией")
    @GetMapping
    public ResponseEntity<Page<OrderDto>> getAllOrders(
            @Parameter(description = "Номер страницы (начиная с 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Поле для сортировки") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Направление сортировки") @RequestParam(defaultValue = "desc") String sortDir) {
        
        log.debug("Получение всех заказов: page={}, size={}, sortBy={}, sortDir={}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> orders = orderService.getAllOrders(pageable);
        Page<OrderDto> orderDtos = orders.map(orderMapper::toDto);
        
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Получить заказы по статусу", description = "Возвращает список заказов с определенным статусом")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDto>> getOrdersByStatus(
            @Parameter(description = "Статус заказа") @PathVariable OrderStatus status) {
        log.debug("Получение заказов по статусу: {}", status);
        
        List<Order> orders = orderService.getOrdersByStatus(status);
        List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
        
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Обновить заказ", description = "Обновляет существующий заказ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Заказ успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные заказа")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderDto> updateOrder(
            @Parameter(description = "ID заказа") @PathVariable Long id,
            @Valid @RequestBody OrderDto orderDto) {
        log.info("Обновление заказа с ID: {}", id);
        
        return orderService.getOrderById(id)
                .map(existingOrder -> {
                    orderMapper.updateEntity(existingOrder, orderDto);
                    Order updatedOrder = orderService.updateOrder(existingOrder);
                    return ResponseEntity.ok(orderMapper.toDto(updatedOrder));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Изменить статус заказа", description = "Изменяет статус существующего заказа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус заказа успешно изменен"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректный переход статуса")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateOrderStatus(
            @Parameter(description = "ID заказа") @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        log.info("Изменение статуса заказа {} на {}", id, request.getNewStatus());
        
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, request.getNewStatus());
            
            // Если указан флорист, назначаем его
            if (request.getAssignedFloristId() != null) {
                updatedOrder = orderService.assignFloristToOrder(id, request.getAssignedFloristId());
            }
            
            return ResponseEntity.ok(orderMapper.toDto(updatedOrder));
        } catch (RuntimeException e) {
            log.error("Ошибка при изменении статуса заказа {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Назначить флориста на заказ", description = "Назначает флориста на выполнение заказа")
    @PatchMapping("/{id}/assign-florist/{floristId}")
    public ResponseEntity<OrderDto> assignFloristToOrder(
            @Parameter(description = "ID заказа") @PathVariable Long id,
            @Parameter(description = "ID флориста") @PathVariable Long floristId) {
        log.info("Назначение флориста {} на заказ {}", floristId, id);
        
        try {
            Order updatedOrder = orderService.assignFloristToOrder(id, floristId);
            return ResponseEntity.ok(orderMapper.toDto(updatedOrder));
        } catch (RuntimeException e) {
            log.error("Ошибка при назначении флориста на заказ {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Отменить заказ", description = "Отменяет существующий заказ")
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderDto> cancelOrder(
            @Parameter(description = "ID заказа") @PathVariable Long id,
            @Parameter(description = "Причина отмены") @RequestParam(required = false) String reason) {
        log.info("Отмена заказа {} по причине: {}", id, reason);
        
        try {
            Order cancelledOrder = orderService.cancelOrder(id, reason);
            return ResponseEntity.ok(orderMapper.toDto(cancelledOrder));
        } catch (RuntimeException e) {
            log.error("Ошибка при отмене заказа {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Удалить заказ", description = "Удаляет заказ из системы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Заказ успешно удален"),
            @ApiResponse(responseCode = "404", description = "Заказ не найден"),
            @ApiResponse(responseCode = "400", description = "Заказ нельзя удалить")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@Parameter(description = "ID заказа") @PathVariable Long id) {
        log.info("Удаление заказа {}", id);
        
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Ошибка при удалении заказа {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Поиск заказов", description = "Поиск заказов по имени клиента или номеру заказа")
    @GetMapping("/search")
    public ResponseEntity<Page<OrderDto>> searchOrders(
            @Parameter(description = "Поисковый запрос") @RequestParam String query,
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "20") int size) {
        
        log.debug("Поиск заказов по запросу: {}", query);
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orders = orderService.searchOrders(query, pageable);
        Page<OrderDto> orderDtos = orders.map(orderMapper::toDto);
        
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Получить заказы клиента по телефону", description = "Возвращает историю заказов клиента")
    @GetMapping("/customer/phone/{phone}")
    public ResponseEntity<List<OrderDto>> getOrdersByCustomerPhone(
            @Parameter(description = "Телефон клиента") @PathVariable String phone) {
        log.debug("Получение заказов клиента по телефону: {}", phone);
        
        List<Order> orders = orderService.getOrdersByCustomerPhone(phone);
        List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
        
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Получить заказы за период", description = "Возвращает заказы за указанный период времени")
    @GetMapping("/date-range")
    public ResponseEntity<List<OrderDto>> getOrdersByDateRange(
            @Parameter(description = "Начальная дата") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Конечная дата") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.debug("Получение заказов за период с {} по {}", startDate, endDate);
        
        List<Order> orders = orderService.getOrdersByDateRange(startDate, endDate);
        List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
        
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Получить заказы, требующие обработки", description = "Возвращает новые и подтвержденные заказы")
    @GetMapping("/requiring-processing")
    public ResponseEntity<List<OrderDto>> getOrdersRequiringProcessing() {
        log.debug("Получение заказов, требующих обработки");
        
        List<Order> orders = orderService.getOrdersRequiringProcessing();
        List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
        
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Получить активные заказы флориста", description = "Возвращает заказы в работе у конкретного флориста")
    @GetMapping("/florist/{floristId}/active")
    public ResponseEntity<List<OrderDto>> getActiveOrdersByFlorist(
            @Parameter(description = "ID флориста") @PathVariable Long floristId) {
        log.debug("Получение активных заказов флориста {}", floristId);
        
        List<Order> orders = orderService.getActiveOrdersByFlorist(floristId);
        List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
        
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Получить заказы готовые к доставке", description = "Возвращает заказы готовые к доставке")
    @GetMapping("/ready-for-delivery")
    public ResponseEntity<List<OrderDto>> getOrdersReadyForDelivery() {
        log.debug("Получение заказов готовых к доставке");
        
        List<Order> orders = orderService.getOrdersReadyForDelivery();
        List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
        
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Получить просроченные заказы", description = "Возвращает заказы с просроченной доставкой")
    @GetMapping("/overdue")
    public ResponseEntity<List<OrderDto>> getOverdueOrders() {
        log.debug("Получение просроченных заказов");
        
        List<Order> orders = orderService.getOverdueOrders();
        List<OrderDto> orderDtos = orderMapper.toDtoList(orders);
        
        return ResponseEntity.ok(orderDtos);
    }

    @Operation(summary = "Получить статистику заказов", description = "Возвращает общую статистику по заказам")
    @GetMapping("/statistics")
    public ResponseEntity<OrderStatisticsDto> getOrderStatistics() {
        log.debug("Получение статистики заказов");
        
        var statistics = orderService.getOrderStatistics();
        OrderStatisticsDto statisticsDto = orderMapper.toDto(statistics);
        
        return ResponseEntity.ok(statisticsDto);
    }

    @Operation(summary = "Получить статистику заказов за период", description = "Возвращает статистику заказов за указанный период")
    @GetMapping("/statistics/date-range")
    public ResponseEntity<OrderStatisticsDto> getOrderStatisticsByDateRange(
            @Parameter(description = "Начальная дата") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Конечная дата") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        
        log.debug("Получение статистики заказов за период с {} по {}", startDate, endDate);
        
        var statistics = orderService.getOrderStatisticsByDateRange(startDate, endDate);
        OrderStatisticsDto statisticsDto = orderMapper.toDto(statistics);
        
        return ResponseEntity.ok(statisticsDto);
    }

    @Operation(summary = "Пересчитать стоимость заказа", description = "Пересчитывает общую стоимость заказа")
    @PatchMapping("/{id}/recalculate")
    public ResponseEntity<OrderDto> recalculateOrderAmount(
            @Parameter(description = "ID заказа") @PathVariable Long id) {
        log.info("Пересчет стоимости заказа {}", id);
        
        try {
            Order recalculatedOrder = orderService.recalculateOrderAmount(id);
            return ResponseEntity.ok(orderMapper.toDto(recalculatedOrder));
        } catch (RuntimeException e) {
            log.error("Ошибка при пересчете стоимости заказа {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
