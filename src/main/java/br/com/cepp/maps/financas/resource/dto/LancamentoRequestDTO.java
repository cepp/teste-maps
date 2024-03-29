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
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public class LancamentoRequestDTO implements Serializable {
    private static final long serialVersionUID = 1540337794210819194L;

    @Digits(integer = 15, fraction = 2, message = "Campo 'valor' inválido")
    @NotNull(message = "Campo 'valor' é obrigatório")
    private final BigDecimal valor;
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Campo 'descricao' inválido")
    @NotEmpty(message = "Campo 'descricao' é obrigatório")
    private final String descricao;
    @JsonDeserialize(using = FinancasLocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FinancasLocalDateDeserializer.DATE_FORMAT)
    @NotNull(message = "Campo 'data' é obrigatório")
    private final LocalDate data;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public LancamentoRequestDTO(@JsonProperty(value = "valor")
                                    @Digits(integer = 15, fraction = 2, message = "Campo 'valor' inválido")
                                    @NotNull(message = "Campo 'valor' é obrigatório") BigDecimal valor,
                                @JsonProperty(value = "descricao")
                                    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "Campo 'descricao' inválido")
                                    @NotEmpty(message = "Campo 'descricao' é obrigatório") String descricao,
                                @JsonProperty(value = "data")
                                    @NotNull(message = "Campo 'data' é obrigatório") LocalDate data) {
        this.valor = valor;
        this.descricao = descricao;
        this.data = data;
    }
}
