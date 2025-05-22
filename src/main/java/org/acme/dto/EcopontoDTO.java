package org.acme.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class EcopontoDTO {
    private Long id;
    private String descricao;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String cep;
    private Boolean aceitaLixoEletronico;
    private Long acessoId;
    private Long enderecoId;
    
    // Campos de acesso
    private String login;
    private String password;
    
    // Campos de endereço
    private String logradouro;
    private String bairro;
    private String complemento;
} 