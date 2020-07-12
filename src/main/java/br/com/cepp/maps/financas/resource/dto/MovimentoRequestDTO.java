package br.com.cepp.maps.financas.resource.dto;

import br.com.cepp.maps.financas.resource.serialization.FinancasLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public class MovimentoRequestDTO implements Serializable {
    private static final long serialVersionUID = -8325791071527501443L;

    @NotEmpty(message = "Campo 'ativo' é obrigatório")
    private final String ativo;
    @JsonDeserialize(using = FinancasLocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FinancasLocalDateDeserializer.DATE_FORMAT)
    @NotNull(message = "Campo 'data' é obrigatório")
    private final LocalDate data;
    @Digits(integer = 8, fraction = 2, message = "Campo 'quantidade' inválido")
    @NotNull(message = "Campo 'quantidade' é obrigatório")
    private final BigDecimal quantidade;
    @Digits(integer = 15, fraction = 2, message = "Campo 'valor' inválido")
    @NotNull(message = "Campo 'valor' é obrigatório")
    private final BigDecimal valor;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MovimentoRequestDTO(@JsonProperty(value = "ativo") @NotEmpty(message = "Campo 'ativo' é obrigatório") String ativo,
                               @JsonProperty(value = "data") @NotNull(message = "Campo 'data' é obrigatório") LocalDate data,
                               @JsonProperty(value = "quantidade") @Digits(integer = 8, fraction = 2, message = "Campo 'quantidade' inválido") @NotNull(message = "Campo 'quantidade' é obrigatório") BigDecimal quantidade,
                               @JsonProperty(value = "valor") @Digits(integer = 15, fraction = 2, message = "Campo 'valor' inválido") @NotNull(message = "Campo 'valor' é obrigatório") BigDecimal valor) {
        this.ativo = ativo;
        this.data = data;
        this.quantidade = quantidade;
        this.valor = valor;
    }
}
