package org.acme.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "logradouro")
    public String logradouro;

    @Column(name = "bairro")
    public String bairro;

    @Column(name = "complemento")
    public String complemento;

    @Column(name = "cep")
    public String cep;
}

