package org.acme.models;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_tipo_lixo_aceito_ecoponto")
public class TipoLixoAceitoEcoponto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "pontos_kg")
    private Double pontosKg;

    @Column(name = "img")
    private String img;

    @Column(name = "aceito")
    private Boolean aceito;

    @ManyToOne
    @JoinColumn(name = "id_ecoponto")
    private Ecoponto ecoponto;

    @ManyToOne
    @JoinColumn(name = "id_tipo_lixo")
    private TipoLixo tipoLixo;
}