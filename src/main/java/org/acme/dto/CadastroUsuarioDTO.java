package org.acme.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

@Data
@RegisterForReflection
public class CadastroUsuarioDTO {
    private String primeiroNome;
    private String ultimoNome;
    private String cpf;
    private String cep;
    private String logradouro;
    private String bairro;
    private String complemento;
    private Long saldoPontos = 0L;
    private String login;
    private String password;
}