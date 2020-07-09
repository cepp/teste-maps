package br.com.cepp.maps.financas.resource.dto;

import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
import br.com.cepp.maps.financas.resource.serialization.FinancasLocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public class EstoqueResponseDTO implements Serializable {
    private static final long serialVersionUID = -6942680256973184802L;

    private final String ativo;
    private final TipoAtivo tipoAtivo;
    private final BigDecimal quantidade;
    @JsonDeserialize(using = FinancasLocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FinancasLocalDateDeserializer.DATE_FORMAT)
    private final LocalDate dataPosicao;
    private final BigDecimal lucro;
    private final BigDecimal rendimento;
    private final BigDecimal valor;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public EstoqueResponseDTO(@JsonProperty(value = "ativo") String ativo,
                              @JsonProperty(value = "tipoAtivo") TipoAtivo tipoAtivo,
                              @JsonProperty(value = "quantidade") BigDecimal quantidade,
                              @JsonProperty(value = "dataPosicao") LocalDate dataPosicao,
                              @JsonProperty(value = "lucro") BigDecimal lucro,
                              @JsonProperty(value = "rendimento") BigDecimal rendimento,
                              @JsonProperty(value = "valor") BigDecimal valor) {
        this.ativo = ativo;
        this.tipoAtivo = tipoAtivo;
        this.quantidade = quantidade;
        this.dataPosicao = dataPosicao;
        this.lucro = lucro;
        this.rendimento = rendimento;
        this.valor = valor;
    }
}
