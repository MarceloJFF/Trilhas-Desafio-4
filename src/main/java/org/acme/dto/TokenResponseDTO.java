package org.acme.dto;

import lombok.Data;

@Data
public class TokenResponseDTO {
    private String token;
    private String tipo;
    private String login;

    public TokenResponseDTO(String token, String tipo, String login) {
        this.token = token;
        this.tipo = tipo;
        this.login = login;
    }
} 