package org.acme.models.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.acme.models.Acesso;

import java.math.BigDecimal;

public class EnderecoDTO {
    public String descricao;
    public BigDecimal latitude;
    public BigDecimal longitude;
    public String cep;
    public Boolean aceitaLixoEletronico;
    public AcessoDTO acesso;
}
