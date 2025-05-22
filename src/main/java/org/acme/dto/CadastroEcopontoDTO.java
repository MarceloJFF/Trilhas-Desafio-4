package org.acme.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CadastroEcopontoDTO {
    private String descricao;
    private String nome;
    private String cnpj;
    private String telefone;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String cep;
    private String logradouro;
    private String bairro;
    private String complemento;
    private String login;
    private String password;
    private Boolean aceitaLixoEletronico=false;
} 