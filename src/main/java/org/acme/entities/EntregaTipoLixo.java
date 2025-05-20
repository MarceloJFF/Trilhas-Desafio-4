package org.acme.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "entrega_tipo_lixo")
public class EntregaTipoLixo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entrega_material_id")
    private EntregaMaterial entregaMaterial;

    @ManyToOne
    @JoinColumn(name = "tipo_lixo_id")
    private TipoLixo tipoLixo;

    private BigDecimal kg;
} 