package org.acme.models;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "tb_eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private LocalDateTime createdAt;
    private Long endereco;
    private String obs;
    private LocalDateTime dataEvento;
    private LocalTime horarioInicioFuncionamento;
    private LocalTime horarioTerminoFuncionamento;
    private Long beneficiosDisponiveis;
    private String foto;

    @ManyToOne
    @JoinColumn(name = "id_ecoponto")
    private Ecoponto ecoponto;
}