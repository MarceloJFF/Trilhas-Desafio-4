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
    private Long id;
    @Column(nullable = false)
    private String login;
    @Column(nullable = false)
    private String password;
    @OneToOne(mappedBy = "acesso")
    private Usuario usuario;
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
