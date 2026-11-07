package com.vidasalud.ms_vidasalud_rabbitmq_admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QueueRequestDTO {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    private String queueName;

    @NotBlank(message = "El nombre del exchange es obligatorio")
    private String exchangeName;

    @NotBlank(message = "La clave de enrutamiento (routing key) es obligatoria")
    private String routingKey;
}