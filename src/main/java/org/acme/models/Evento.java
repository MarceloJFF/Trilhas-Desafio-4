package org.acme.models;
import jakarta.persistence.*;
import lombok.Data;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "tb_eventos")
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="descricao")
    private String descricao;
    @Column(name="created_at")
    private LocalDateTime createdAt;
    
    @ManyToOne
    @JoinColumn(name = "endereco")
    private Endereco endereco;
    @Column(name="obs")
    private String obs;
    @Column(name="data_evento")
    private LocalDate dataEvento;
    @Column(name="horario_inicio_funcionamento")
    private LocalTime horarioInicioFuncionamento;
    @Column(name="horario_termino_funcionamento")
    private LocalTime horarioTerminoFuncionamento;
   
    @Column(name="foto")
    private String foto;
    @ManyToOne
    @JoinColumn(name = "id_ecoponto")
    private Ecoponto ecoponto;
}