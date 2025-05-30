package org.acme.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Data;
@Data
@Table(name = "tb_acesso")
@Entity
public class Acesso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="login",nullable = false)
    private String login;
    @Column(name="password",nullable = false)
    private String password;
    @OneToOne(mappedBy = "acesso")
    private Usuario usuario;
    @Column(name="tipo",nullable = true)
    private String tipo;
    @OneToOne(mappedBy = "acesso")
    @JsonIgnore
    private Ecoponto ecoponto;
    @Override
    public String toString() {
        return "Acesso{" +
                "id=" + id +
                ", login='" + login + '\'' +
                // NÃO inclua ecoponto.toString() aqui!
                '}';
    }
}
