package org.acme.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class InscricaoDTO {
    private Long id;
    private LocalDateTime data;
    private Long usuarioId;
    private Long eventoId;
} 