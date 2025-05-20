package org.acme.dto;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private Long pontos;
    private Long acessoId;
    private Long enderecoId;
} 