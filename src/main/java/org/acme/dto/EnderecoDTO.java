package org.acme.dto;

import lombok.Data;

@Data
public class EnderecoDTO {
    private Long id;
    private String logradouro;
    private String bairro;
    private String complemento;
} 