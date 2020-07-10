package br.com.cepp.maps.financas;

import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestTestDTO;
import br.com.cepp.maps.financas.resource.dto.AtivoValorRequestTestDTO;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestTestDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestTestDTO;
import br.com.cepp.maps.financas.resource.serialization.FinancasLocalDateDeserializer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.util.Strings;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        return new LancamentoRequestDTO(valorValido.setScale(0, RoundingMode.DOWN),
                RandomStringUtils.random(10, true, false), LocalDate.now());
    }

    protected AtivoRequestDTO getAtivoRequestDTO() {
        return this.getAtivoRequestDTO(Strings.EMPTY);
    }

    protected AtivoRequestDTO getAtivoRequestDTO(String codigo) {
        return this.getAtivoRequestDTO(codigo, TipoAtivo.RV, null);
    }

    protected AtivoRequestDTO getAtivoRequestDTO(TipoAtivo tipoAtivo) {
        return this.getAtivoRequestDTO(null, tipoAtivo, null);
    }

    protected AtivoRequestDTO getAtivoRequestDTO(LocalDate dataEmissao) {
        return this.getAtivoRequestDTO(null, TipoAtivo.RV, dataEmissao);
    }

    protected AtivoRequestDTO getAtivoRequestDTO(String codigo, TipoAtivo tipoAtivo, LocalDate dataEmissao) {
        String codigoAtivo = Strings.isEmpty(codigo) ? RandomStringUtils.random(8, true, true) : codigo;
        String nome = RandomStringUtils.random(10, true, true);
        LocalDate novaDataEmissao = dataEmissao == null ? LocalDate.now() : dataEmissao;
        return new AtivoRequestDTO(codigoAtivo, nome, tipoAtivo, novaDataEmissao, LocalDate.now());
    }

    protected AtivoRequestTestDTO getAtivoRequestDTOMock() {
        AtivoRequestTestDTO ativoRequest = new AtivoRequestTestDTO();
        ativoRequest.setCodigo(RandomStringUtils.random(8, true, true));
        ativoRequest.setTipoAtivo(TipoAtivo.RV.name());
        ativoRequest.setNome(RandomStringUtils.random(10, true, true));
        ativoRequest.setDataEmissao(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        ativoRequest.setDataVencimento(LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE));
        return ativoRequest;
    }

    protected MovimentoRequestTestDTO getMovimentoRequestTestDTOMock(String ativo) {
        return this.getMovimentoRequestTestDTOMock(ativo, LocalDate.now());
    }

    protected MovimentoRequestTestDTO getMovimentoRequestTestDTOMock(String ativo, LocalDate data) {
        return this.getMovimentoRequestTestDTOMock(ativo, data, 999999);
    }

    protected MovimentoRequestTestDTO getMovimentoRequestTestDTOMock(String ativo, LocalDate data, Integer quantidadeMax) {
        MovimentoRequestTestDTO movimentoRequestTestDTO = new MovimentoRequestTestDTO();
        movimentoRequestTestDTO.setAtivo(ativo);
        BigDecimal quantidade = BigDecimal.valueOf(RandomUtils.nextDouble(1, quantidadeMax)).setScale(2, RoundingMode.HALF_DOWN);
        movimentoRequestTestDTO.setQuantidade(String.valueOf(quantidade.doubleValue()));
        movimentoRequestTestDTO.setData(data.format(DateTimeFormatter.ISO_DATE));
        return movimentoRequestTestDTO;
    }

    protected AtivoValorRequestTestDTO getAtivoValorRequestTestDTOMock() {
        AtivoValorRequestTestDTO ativoRequestTestDTO = new AtivoValorRequestTestDTO();
        ativoRequestTestDTO.setData(LocalDate.now().format(DateTimeFormatter.ISO_DATE));
        BigDecimal valor = BigDecimal.valueOf(RandomUtils.nextDouble(1, 999999)).setScale(8, RoundingMode.DOWN);
        ativoRequestTestDTO.setValor(String.valueOf(valor.doubleValue()));
        ativoRequestTestDTO.setCodigoAtivo(RandomStringUtils.random(10, true, true));
        return ativoRequestTestDTO;
    }
}
