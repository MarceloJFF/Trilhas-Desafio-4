package org.acme.models;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tb_beneficios")
public class Beneficio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private Long qtdPontosNecessarios;
    private String empresa;
    private LocalDateTime dataExpiracao;
    private Boolean expirado;

    @ManyToOne
    @JoinColumn(name = "id_ecoponto")
    private Ecoponto ecoponto;

    @PrePersist
    @PreUpdate
    public void atualizarStatusExpiracao() {
        if (dataExpiracao != null) {
            this.expirado = LocalDateTime.now().isAfter(dataExpiracao);
        }
    }
}
