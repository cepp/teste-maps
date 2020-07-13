package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.AbstractDataTest;
import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.AtivoPeriodoInvalidoException;
import br.com.cepp.maps.financas.resource.handler.MovimentoFinalSemanaException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static br.com.cepp.maps.financas.config.AplicacaoConfig.CODIGO_USUARIO_GLOBAL;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MovimentoServiceTest extends AbstractDataTest {

    @Autowired
    private MovimentoService service;


    @Test
    void compra() {
        final LocalDate dataDepoisVencimento = super.getDataDiaUtil().plusDays(5);
        final String codigo = RandomStringUtils.random(8, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, dataDepoisVencimento);
        MovimentoRequestDTO movimentoDataDepoisVencimento = super.getMovimentoRequestDTOMock(codigo, dataDepoisVencimento);
        assertThrows(AtivoPeriodoInvalidoException.class, () -> service.venda(movimentoDataDepoisVencimento, CODIGO_USUARIO_GLOBAL));
        assertThrows(AtivoPeriodoInvalidoException.class, () -> service.compra(movimentoDataDepoisVencimento, CODIGO_USUARIO_GLOBAL));
    }

    @Test
    void compraValidarData() {
        final LocalDate dataEmissao = super.getDataDiaUtil();
        final String codigo = RandomStringUtils.random(8, true, true);

        final LocalDate dataAnteriorEmissao = dataEmissao.minusDays(3);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, dataAnteriorEmissao);
        MovimentoRequestDTO movimentoDataAnteriorEmissao = super.getMovimentoRequestDTOMock(codigo, dataAnteriorEmissao);
        assertThrows(AtivoPeriodoInvalidoException.class, () -> service.compra(movimentoDataAnteriorEmissao, CODIGO_USUARIO_GLOBAL));

        final LocalDate finalSemana = dataEmissao.plusDays(1);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, finalSemana);
        MovimentoRequestDTO movimentoFinalSemana = super.getMovimentoRequestDTOMock(codigo, finalSemana);
        assertThrows(MovimentoFinalSemanaException.class, () -> service.compra(movimentoFinalSemana, CODIGO_USUARIO_GLOBAL));

        final LocalDate dataDepoisVencimento = super.getDataDiaUtil().plusDays(5);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, dataDepoisVencimento);
        MovimentoRequestDTO movimentoDataDepoisVencimento = super.getMovimentoRequestDTOMock(codigo, dataDepoisVencimento);
        assertThrows(AtivoPeriodoInvalidoException.class, () -> service.compra(movimentoDataDepoisVencimento, CODIGO_USUARIO_GLOBAL));
    }

    @Test
    void venda() {
        final LocalDate dataDepoisVencimento = super.getDataDiaUtil().plusDays(5);
        final String codigo = RandomStringUtils.random(8, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, dataDepoisVencimento);
        MovimentoRequestDTO movimentoDataDepoisVencimento = super.getMovimentoRequestDTOMock(codigo, dataDepoisVencimento);
        assertThrows(AtivoPeriodoInvalidoException.class, () -> service.venda(movimentoDataDepoisVencimento, CODIGO_USUARIO_GLOBAL));
    }

}
