package org.acme.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class EventoDTO {
    private Long id;
    private String descricao;
    private LocalDateTime createdAt;
    private Long enderecoId;
    private String obs;
    private LocalDateTime dataEvento;
    private LocalTime horarioInicioFuncionamento;
    private LocalTime horarioTerminoFuncionamento;
    private Long beneficiosDisponiveis;
    private String foto;
    private Long ecopontoId;
} 