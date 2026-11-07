package com.vidasalud.ms_vidasalud_rabbitmq_admin.controller;

import com.vidasalud.ms_vidasalud_rabbitmq_admin.dto.QueueRequestDTO;
import com.vidasalud.ms_vidasalud_rabbitmq_admin.service.RabbitAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/queues")
public class RabbitAdminController {

    private final RabbitAdminService rabbitAdminService;

    public RabbitAdminController(RabbitAdminService rabbitAdminService) {
        this.rabbitAdminService = rabbitAdminService;
    }

    @PostMapping
    public ResponseEntity<String> createQueue(@Valid @RequestBody QueueRequestDTO request) {
        rabbitAdminService.createQueueAndBinding(
                request.getQueueName(),
                request.getExchangeName(),
                request.getRoutingKey()
        );
        return ResponseEntity.ok("Cola '" + request.getQueueName() + "' creada exitosamente con soporte DLQ.");
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteQueue(@PathVariable String name) {
        rabbitAdminService.deleteQueue(name);
        return ResponseEntity.ok("Cola '" + name + "' y su DLQ eliminadas exitosamente.");
    }
}