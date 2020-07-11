package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.AbstractDataTest;
import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.AtivoPeriodoInvalidoException;
import br.com.cepp.maps.financas.resource.handler.MovimentoFinalSemanaException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static br.com.cepp.maps.financas.config.AplicacaoConfig.CODIGO_USUARIO_GLOBAL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MovimentoServiceTest extends AbstractDataTest {

    @Autowired
    private MovimentoService service;
    @Autowired
    private AtivoService ativoService;

    @Test
    void compra() {

    }

    @Test
    void compraValidarData() {
        final LocalDate dataEmissao = super.getDataDiaUtil();
        final LocalDate dataVencimento = dataEmissao.plusDays(4);
        Ativo ativo = assertDoesNotThrow(() -> this.ativoService.incluir(super.getAtivoRequestDTO(dataEmissao, dataVencimento)));

        final LocalDate dataAnteriorEmissao = dataEmissao.minusDays(3);
        MovimentoRequestDTO movimentoDataAnteriorEmissao = super.getMovimentoRequestDTOMock(ativo.getCodigo(), dataAnteriorEmissao);
        assertThrows(AtivoPeriodoInvalidoException.class, () -> service.compra(movimentoDataAnteriorEmissao, CODIGO_USUARIO_GLOBAL));

        final LocalDate finalSemana = dataEmissao.plusDays(1);
        MovimentoRequestDTO movimentoFinalSemana = super.getMovimentoRequestDTOMock(ativo.getCodigo(), finalSemana);
        assertThrows(MovimentoFinalSemanaException.class, () -> service.compra(movimentoFinalSemana, CODIGO_USUARIO_GLOBAL));

        final LocalDate dataDepoisVencimento = dataVencimento.plusDays(1);
        MovimentoRequestDTO movimentoDataDepoisVencimento = super.getMovimentoRequestDTOMock(ativo.getCodigo(), dataDepoisVencimento);
        assertThrows(AtivoPeriodoInvalidoException.class, () -> service.compra(movimentoDataDepoisVencimento, CODIGO_USUARIO_GLOBAL));
    }

    @Test
    void venda() {
    }

    @Test
    void buscarPosicaoPorCodigoEData() {
    }

    @Test
    void buscarPorDataPosicao() {
    }
}
