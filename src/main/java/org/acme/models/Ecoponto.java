package org.acme.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Entity
@Table(name = "tb_ecoponto")
public class Ecoponto {

    @Id
    @GeneratedValue(strategy =
            GenerationType.IDENTITY)
    @Column(name="id")
    public Long id;
    @Column(name="descricao",nullable = false)
    public String descricao;

    @Column(nullable = false,name ="latitude",  precision = 8, scale = 2)
    public BigDecimal latitude;

    @Column(nullable = false,name ="longitude", precision = 8, scale = 2)
    public BigDecimal longitude;
  
    @Column(nullable = false,name ="cep")
    public String cep;

    @Column(name = "aceita_lixo_eletronico", nullable = true)
    public Boolean aceitaLixoEletronico;

    @OneToOne
    @JoinColumn(name = "id_acesso", nullable = false)
    public Acesso acesso;
    @OneToOne
    @JoinColumn(name = "id_endereco", nullable = false)
    public Endereco endereco;
    @Column(name = "nome", nullable = false)
    private String nome;
    @Column(name = "telefone", nullable = false)
    private String Telefone;
    @Column(name = "cnpj", nullable = false)
    private String cnpj;
    @Override
    public String toString() {
        return "Ecoponto{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", cep='" + cep + '\'' +
                // NÃO inclua acesso.toString() aqui!
                '}';
    }

}
