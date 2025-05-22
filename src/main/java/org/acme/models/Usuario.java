package org.acme.models;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Column(name = "id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "primeiro_nome", nullable = false)
    private String primeiroNome;
    @Column(name = "ultimo_nome", nullable = false)
    private String ultimoNome;
    @Column(name = "is_admin", nullable = true)
    private Boolean isAdmin;
    @Column(name = "cpf", nullable = false)
    private String cpf;
    @Column(name = "saldo_pontos", nullable = false)
    private Long saldoPontos=0L;
    @OneToOne @JoinColumn(name = "id_endereco")
    private Endereco endereco;
    @JsonIgnore
    @OneToOne @JoinColumn(name = "id_acesso")
    private Acesso acesso;
}
