package org.acme.models;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_entrega_material")
public class EntregaMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;
    @Column(name = "qtd_pontos_gerados")
    private Long qtdPontosGerados;
    @Column(name = "id_empresa")
    private Long idEmpresa;
    @Column(name = "valor_total_gerado")
    private Long valorTotalGerado;
    @Column(name = "deferido")
    private Boolean deferido;
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
