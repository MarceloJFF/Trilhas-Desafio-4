package org.acme.dto;

import lombok.Data;

@Data
public class CadastroEmpresaDTO {
    private String login;
    private String password;
    private String nome;
    private String cnpj;
    private String cep;
    private String logradouro;
    private String bairro;
    private String complemento;
} 