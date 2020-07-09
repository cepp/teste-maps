package br.com.cepp.maps.financas.resource.dto;

import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
public class AtivoRequestDTO implements Serializable {
    private static final long serialVersionUID = -2394763346833853202L;

    @NotEmpty(message = "Campo 'codigo' é obrigatório")
    private final String codigo;
    @NotEmpty(message = "Campo 'nome' é obrigatório")
    private final String nome;
    @NotNull(message = "Campo 'tipoAtivo' é obrigatório")
    @Enumerated(EnumType.STRING)
    private final TipoAtivo tipoAtivo;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public AtivoRequestDTO(@JsonProperty(value = "codigo") @NotEmpty(message = "Campo 'codigo' é obrigatório") String codigo,
                           @JsonProperty(value = "nome") @NotEmpty(message = "Campo 'nome' é obrigatório") String nome,
                           @JsonProperty(value = "tipoAtivo") @NotNull(message = "Campo 'tipoAtivo' é obrigatório") TipoAtivo tipoAtivo) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipoAtivo = tipoAtivo;
    }
}
