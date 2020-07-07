package br.com.cepp.maps.financas;

import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestTestDTO;
import br.com.cepp.maps.financas.resource.serialization.FinancasLocalDateDeserializer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public abstract class AbstractDataTest {
    protected LancamentoRequestTestDTO getLancamentoRequestRestMock() {
        LancamentoRequestTestDTO lancamento = new LancamentoRequestTestDTO();
        lancamento.setData(LocalDateTime.now().format(DateTimeFormatter.ofPattern(FinancasLocalDateDeserializer.DATE_FORMAT)));
        lancamento.setDescricao(RandomStringUtils.random(10, true, false));
        lancamento.setValor(RandomStringUtils.random(5, false, true));
        return lancamento;
    }

    protected LancamentoRequestDTO getLancamentoRequestMock() {
        return this.getLancamentoRequestMock(null, null);
    }

    protected LancamentoRequestDTO getLancamentoRequestMock(final BigDecimal valor) {
        return this.getLancamentoRequestMock(valor, null);
    }

    protected LancamentoRequestDTO getLancamentoRequestMock(final BigDecimal valor, final String codigoUsuario) {
        LancamentoRequestDTO lancamento = new LancamentoRequestDTO();
        lancamento.setData(LocalDate.now());
        lancamento.setDescricao(RandomStringUtils.random(10, true, false));
        BigDecimal valorValido = valor == null ? BigDecimal.valueOf(RandomUtils.nextDouble(1.0, 99999.299)) : valor;
        lancamento.setValor(valorValido);
        lancamento.setCodigoUsuario(Strings.isEmpty(codigoUsuario) ? UUID.randomUUID().toString() : codigoUsuario);
        return lancamento;
    }
}
