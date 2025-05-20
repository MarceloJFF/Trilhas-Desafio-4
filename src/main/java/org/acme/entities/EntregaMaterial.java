package org.acme.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity
@Table(name = "entrega_material")
public class EntregaMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ecoponto_id")
    private Ecoponto ecoponto;

    private Long dataEntrega;
    private Long qtdPontosGerados;
    private Long idEmpresa;
    private Long valorTotalGerado;

    @OneToMany(mappedBy = "entregaMaterial", cascade = CascadeType.ALL)
    private List<EntregaTipoLixo> tiposLixo;
} 