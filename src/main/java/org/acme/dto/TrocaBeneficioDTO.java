package org.acme.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@RegisterForReflection
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