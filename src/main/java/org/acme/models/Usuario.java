package org.acme.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_usuario")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String primeiroNome;
    private String ultimoNome;
    private Boolean isAdmin;
    private String cpf;
    private Long saldoPontos;

    @OneToOne @JoinColumn(name = "id_endereco")
    private Endereco endereco;
    @OneToOne @JoinColumn(name = "id_acesso")
    private Acesso acesso;
}
