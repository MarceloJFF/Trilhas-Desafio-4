package org.acme.dto;

import lombok.Data;

@Data
public class TipoLixoAceitoEcopontoDTO {
    private Long id;
    private Double pontosKg;
    private String img;
    private Long ecopontoId;
    private Long tipoLixoId;
    private Boolean aceito;
} 