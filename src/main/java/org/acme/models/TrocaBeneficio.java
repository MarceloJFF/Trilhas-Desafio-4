package org.acme.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "tb_troca_beneficio")
public class TrocaBeneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "qtd_pontos_consumidos")
    private Long qtdPontosConsumidos;
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_beneficio")
    private Beneficio beneficio;

    @ManyToOne
    @JoinColumn(name = "id_ecoponto")
    private Ecoponto ecoponto;
    @Column(name = "dataTroca")
    private LocalDateTime dataTroca;
}
