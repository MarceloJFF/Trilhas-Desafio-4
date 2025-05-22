package org.acme.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class EntregaMaterialDTO {
    private Long id;
    private Long idUsuario;
    private Long idEcoponto;
    private Long dataEntrega;
    private Long qtdPontosGerados;
    private Long idEmpresa;
    private Long valorTotalGerado;
    private Boolean deferido;
    private int tipoLixoId;
    private BigDecimal kg;
} 