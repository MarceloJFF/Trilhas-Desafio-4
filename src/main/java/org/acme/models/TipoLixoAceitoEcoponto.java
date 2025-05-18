package org.acme.models;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_tipo_lixo_aceito_ecoponto")
public class TipoLixoAceitoEcoponto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double pontosKg;
    private String img;

    @ManyToOne
    @JoinColumn(name = "id_ecoponto")
    private Ecoponto ecoponto;

    @ManyToOne
    @JoinColumn(name = "id_tipo_lixo")
    private TipoLixo tipoLixo;
}