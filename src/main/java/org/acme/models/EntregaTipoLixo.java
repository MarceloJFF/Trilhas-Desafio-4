package org.acme.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tb_entrega_tipo_lixo")
public class EntregaTipoLixo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal kg;

    @ManyToOne
    @JoinColumn(name = "id_tipo__lixo")
    private TipoLixo tipoLixo;

    @ManyToOne
    @JoinColumn(name = "id_entrega_material")
    private EntregaMaterial entregaMaterial;
}
