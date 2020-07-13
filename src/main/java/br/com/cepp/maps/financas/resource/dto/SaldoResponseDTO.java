package br.com.cepp.maps.financas.resource.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@ToString
@EqualsAndHashCode
public class SaldoResponseDTO implements Serializable {
    private static final long serialVersionUID = 8518155050617599364L;

    private final BigDecimal saldo;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public SaldoResponseDTO(@JsonProperty(value = "saldo") BigDecimal saldo) {
        this.saldo = saldo;
    }
}
