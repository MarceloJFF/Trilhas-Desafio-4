package org.acme.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_entrega_material")
public class EntregaMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long dataEntrega;
    private Long qtdPontosGerados;
    private Long idEmpresa;
    private Long valorTotalGerado;

    @ManyToOne
    @JoinColumn(name = "id_user")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_ecoponto")
    private Ecoponto ecoponto;
    @ManyToOne
    @JoinColumn(name = "id_status")
    private StatusSolicitacao status;

}
