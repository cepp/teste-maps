package br.com.cepp.maps.financas.utils;

import br.com.cepp.maps.financas.model.dominio.TipoMovimento;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BigDecimalUtils {
    public static BigDecimal ajustarValor(final BigDecimal valor, final BigDecimal valorAcumulado, final TipoMovimento tipoMovimento) {
        final BigDecimal valorAjustado = TipoMovimento.COMPRA.equals(tipoMovimento) ? valor.negate() : valor;
        return valorAcumulado
                .add(valorAjustado)
                .setScale(2, RoundingMode.DOWN);
    }
}
