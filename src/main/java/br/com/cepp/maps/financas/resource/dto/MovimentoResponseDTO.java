package br.com.cepp.maps.financas.resource.dto;

import br.com.cepp.maps.financas.model.dominio.TipoMovimento;
import br.com.cepp.maps.financas.resource.serialization.FinancasLocalDateDeserializer;
import br.com.cepp.maps.financas.resource.serialization.FinancasLocalDateSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public class MovimentoResponseDTO implements Serializable {
    private static final long serialVersionUID = 4405642187827518911L;

    private final String ativo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FinancasLocalDateDeserializer.DATE_FORMAT)
    @JsonSerialize(using = FinancasLocalDateSerializer.class)
    private final LocalDate dataMovimento;
    private final BigDecimal quantidade;
    private final BigDecimal valor;
    private final TipoMovimento tipoMovimento;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MovimentoResponseDTO(@JsonProperty(value = "ativo") String ativo,
                                @JsonProperty(value = "dataMovimento") LocalDate dataMovimento,
                                @JsonProperty(value = "quantidade") BigDecimal quantidade,
                                @JsonProperty(value = "valor") BigDecimal valor,
                                @JsonProperty(value = "tipoMovimento") TipoMovimento tipoMovimento) {
        this.ativo = ativo;
        this.dataMovimento = dataMovimento;
        this.quantidade = quantidade;
        this.valor = valor;
        this.tipoMovimento = tipoMovimento;
    }
}
