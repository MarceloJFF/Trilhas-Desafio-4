package org.acme.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tipo_lixo_aceito_ecoponto")
public class TipoLixoAceitoEcoponto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ecoponto_id")
    private Ecoponto ecoponto;

    @ManyToOne
    @JoinColumn(name = "tipo_lixo_id")
    private TipoLixo tipoLixo;

    private Double pontosPorKg;
    private BigDecimal valorPorKg;
} 