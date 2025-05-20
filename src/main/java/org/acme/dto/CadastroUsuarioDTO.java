package org.acme.dto;

import lombok.Data;

@Data
public class CadastroUsuarioDTO {
    private String login;
    private String password;
    private String primeiroNome;
    private String ultimoNome;
    private String cpf;
    private String cep;
    private String logradouro;
    private String bairro;
    private String complemento;
} 