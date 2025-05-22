package org.acme.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.time.LocalDate;

@Data
public class EventoDTO {
    private Long id;
    private String descricao;
    private LocalDateTime createdAt;
    private String endereco;
    private String obs;
    private LocalDate dataEvento;
    private LocalTime horarioInicioFuncionamento;
    private LocalTime horarioTerminoFuncionamento;
    private String foto;
    private String loginUsuario;
} 