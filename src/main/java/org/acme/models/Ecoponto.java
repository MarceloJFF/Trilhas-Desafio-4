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
    public Long id;

    @Column(nullable = false)
    public String descricao;

    @Column(nullable = false, precision = 8, scale = 2)
    public BigDecimal latitude;

    @Column(nullable = false, precision = 8, scale = 2)
    public BigDecimal longitude;

    @Column(nullable = false)
    public String cep;

    @Column(name = "aceita_lixo_eletronico", nullable = false)
    public Boolean aceitaLixoEletronico;

    @OneToOne
    @JoinColumn(name = "id_acesso", nullable = false)
    public Acesso acesso;
    @OneToOne
    @JoinColumn(name = "id_endereco", nullable = false)
    public Endereco endereco;

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
