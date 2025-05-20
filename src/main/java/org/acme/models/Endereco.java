package org.acme.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public String logradouro;

    @Column(nullable = false)
    public String bairro;

    @Column(nullable = false)
    public String complemento;

    @Column(nullable = false)
    public String cep;
}

