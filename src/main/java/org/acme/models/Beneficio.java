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
    @Column(name = "id")
    private Long id;
    @Column(name = "descricao")
    private String descricao;
    @Column(name = "qtd_pontos_necessarios")
    private Long qtdPontosNecessarios;
    @Column(name = "empresa")
    private String empresa;
    @Column(name = "data_expiracao")
    private LocalDateTime dataExpiracao;

    @Column(name = "status")
    private String status;

    @Column(name = "expirado")
    private Boolean expirado;
    @Column(name = "login_usuario")
    private String loginUsuario;
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
