package org.acme.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BeneficioDTO {
    private Long id;
    private String descricao;
    private Long qtdPontosNecessarios;
    private String empresa;
    private LocalDateTime dataExpiracao;
    private Long ecopontoId;
} 