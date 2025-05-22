package org.acme.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class EntregaTipoLixoDTO {
    private Long id;
    private BigDecimal kg;
    private Long tipoLixoId;
    private Long entregaMaterialId;
} 