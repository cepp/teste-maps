package br.com.cepp.maps.financas.resource.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class MovimentoRequestDTO implements Serializable {
    private static final long serialVersionUID = -8325791071527501443L;

    @NotEmpty(message = "Campo 'ativo' é obrigatório")
    private String ativo;
    @NotNull(message = "Campo 'data' é obrigatório")
    private Date data;
    @NotNull(message = "Campo 'quantidade' é obrigatório")
    private BigDecimal quantidade;
    @NotNull(message = "Campo 'valor' é obrigatório")
    private BigDecimal valor;
}
