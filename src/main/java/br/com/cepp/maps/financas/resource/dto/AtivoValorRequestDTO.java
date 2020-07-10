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
public class AtivoValorRequestDTO implements Serializable {
    private static final long serialVersionUID = -2394763346833853202L;

    @NotEmpty(message = "Campo 'codigoAtivo' é obrigatório")
    private final String codigoAtivo;
    @JsonDeserialize(using = FinancasLocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FinancasLocalDateDeserializer.DATE_FORMAT)
    @NotNull(message = "Campo 'data' é obrigatório")
    private final LocalDate data;
    @Digits(integer = 8, fraction = 8, message = "Campo 'valor' inválido")
    @NotNull(message = "Campo 'valor' é obrigatório")
    private final BigDecimal valor;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AtivoValorRequestDTO(@JsonProperty(value = "codigoAtivo") @NotEmpty(message = "Campo 'codigo' é obrigatório") String codigo,
                                @JsonProperty(value = "data") @NotNull(message = "Campo 'dataEmissao' é obrigatório") LocalDate data,
                                @JsonProperty(value = "valor")
                                    @Digits(integer = 8, fraction = 8, message = "Campo 'valor' inválido")
                                    @NotNull(message = "Campo 'valor' é obrigatório") BigDecimal valor) {
        this.codigoAtivo = codigo;
        this.data = data;
        this.valor = valor;
    }
}
