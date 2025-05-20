package org.acme.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TrocaBeneficioDTO {
    private Long id;
    private Long idUsuario;
    private String nomeUsuario;
    private Long idBeneficio;
    private String descricaoBeneficio;
    private Long idEcoponto;
    private String nomeEcoponto;
    private Long qtdPontosConsumidos;
    private LocalDateTime dataTroca;
} 