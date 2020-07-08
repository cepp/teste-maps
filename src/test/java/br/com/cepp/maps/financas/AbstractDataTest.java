package br.com.cepp.maps.financas;

import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestTestDTO;
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

public abstract class AbstractDataTest {
    protected LancamentoRequestTestDTO getLancamentoRequestRestMock() {
        LancamentoRequestTestDTO lancamento = new LancamentoRequestTestDTO();
        lancamento.setData(LocalDateTime.now().format(DateTimeFormatter.ofPattern(FinancasLocalDateDeserializer.DATE_FORMAT)));
        lancamento.setDescricao(RandomStringUtils.random(10, true, false));
        lancamento.setValor(RandomStringUtils.random(2, false, true));
        return lancamento;
    }

    protected LancamentoRequestDTO getLancamentoRequestMock() {
        return this.getLancamentoRequestMock(null);
    }

    protected LancamentoRequestDTO getLancamentoRequestMock(final BigDecimal valor) {
        BigDecimal valorValido = valor == null ? BigDecimal.valueOf(RandomUtils.nextDouble(1.0, 99999.299)) : valor;
        return new LancamentoRequestDTO(valorValido, RandomStringUtils.random(10, true, false), LocalDate.now());
    }

    protected AtivoRequestDTO getAtivoRequestDTO() {
        return this.getAtivoRequestDTO(null);
    }

    protected AtivoRequestDTO getAtivoRequestDTO(String codigo) {
        String codigoAtivo = Strings.isEmpty(codigo) ? RandomStringUtils.random(8, true, true) : codigo;
        String nome = RandomStringUtils.random(10, true, true);
        return new AtivoRequestDTO(codigoAtivo, BigDecimal.TEN, nome, TipoAtivo.RV);
    }

    protected AtivoRequestTestDTO getAtivoRequestDTOMock() {
        AtivoRequestTestDTO ativoRequest = new AtivoRequestTestDTO();
        ativoRequest.setCodigo(RandomStringUtils.random(8, true, true));
        ativoRequest.setPreco(RandomStringUtils.random(8, false, true));
        ativoRequest.setTipoAtivo(TipoAtivo.RV.name());
        ativoRequest.setNome(RandomStringUtils.random(10, true, true));
        return ativoRequest;
    }

    protected AtivoRequestTestDTO getAtivoRequestDTOMock(String codigo) {
        AtivoRequestTestDTO ativoRequest = new AtivoRequestTestDTO();
        String codigoAtivo = Strings.isEmpty(codigo) ? RandomStringUtils.random(8, true, true) : codigo;
        ativoRequest.setCodigo(codigoAtivo);
        ativoRequest.setPreco(RandomStringUtils.random(8, false, true));
        ativoRequest.setTipoAtivo(TipoAtivo.RV.name());
        return ativoRequest;
    }
}
