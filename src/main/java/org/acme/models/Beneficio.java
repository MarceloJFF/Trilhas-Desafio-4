package org.acme.models;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_beneficios")
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long descricao;
    private Long qtdPontosNecessarios;
    private String empresa;

    @ManyToOne
    @JoinColumn(name = "id_ecoponto")
    private Ecoponto ecoponto;
}
