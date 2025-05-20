package org.acme.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_entrega_material_status")
public class StatusSolicitacao {
    @Id
    private int id;
    private String nome;
}