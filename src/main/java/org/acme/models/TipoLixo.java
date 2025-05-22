package org.acme.models;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_tipo_lixo")
public class TipoLixo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "descricao")
    private String descricao;
}