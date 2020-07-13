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

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@ToString
@EqualsAndHashCode
public class AtivoRequestDTO implements Serializable {
    private static final long serialVersionUID = -2394763346833853202L;

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Campo 'codigo' inválido")
    @NotEmpty(message = "Campo 'codigo' é obrigatório")
    private final String codigo;
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Campo 'nome' inválido")
    @NotEmpty(message = "Campo 'nome' é obrigatório")
    private final String nome;
    @NotNull(message = "Campo 'tipoAtivo' é obrigatório")
    @Enumerated(EnumType.STRING)
    private final TipoAtivo tipoAtivo;
    @JsonDeserialize(using = FinancasLocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FinancasLocalDateDeserializer.DATE_FORMAT)
    @NotNull(message = "Campo 'dataEmissao' é obrigatório")
    private final LocalDate dataEmissao;
    @JsonDeserialize(using = FinancasLocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = FinancasLocalDateDeserializer.DATE_FORMAT)
    @NotNull(message = "Campo 'dataVencimento' é obrigatório")
    private final LocalDate dataVencimento;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AtivoRequestDTO(@JsonProperty(value = "codigo")
                               @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Campo 'codigo' inválido")
                               @NotEmpty(message = "Campo 'codigo' é obrigatório") String codigo,
                           @JsonProperty(value = "nome")
                               @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Campo 'nome' inválido")
                               @NotEmpty(message = "Campo 'nome' é obrigatório") String nome,
                           @JsonProperty(value = "tipoAtivo")
                               @NotNull(message = "Campo 'tipoAtivo' é obrigatório") TipoAtivo tipoAtivo,
                           @JsonProperty(value = "dataEmissao")
                               @NotNull(message = "Campo 'dataEmissao' é obrigatório") LocalDate dataEmissao,
                           @JsonProperty(value = "dataVencimento")
                               @NotNull(message = "Campo 'dataVencimento' é obrigatório") LocalDate dataVencimento) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipoAtivo = tipoAtivo;
        this.dataEmissao = dataEmissao;
        this.dataVencimento = dataVencimento;
    }
}
