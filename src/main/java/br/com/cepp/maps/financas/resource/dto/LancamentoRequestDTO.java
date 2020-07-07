package br.com.cepp.maps.financas.resource.dto;

import br.com.cepp.maps.financas.resource.serialization.FinancasLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class LancamentoRequestDTO implements Serializable {
    private static final long serialVersionUID = 1540337794210819194L;

    @NotNull(message = "Campo 'valor' é obrigatório")
    private String valor;
    @NotEmpty(message = "Campo 'descricao' é obrigatório")
    private String descricao;
    @JsonDeserialize(using = FinancasLocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FinancasLocalDateDeserializer.DATE_FORMAT)
    @NotNull(message = "Campo 'data' é obrigatório")
    private LocalDate data;
}
