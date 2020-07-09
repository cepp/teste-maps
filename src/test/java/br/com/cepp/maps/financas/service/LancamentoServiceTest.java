package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.AbstractDataTest;
import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static br.com.cepp.maps.financas.config.AppDataConfig.CODIGO_USUARIO_GLOBAL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class LancamentoServiceTest extends AbstractDataTest {

    @Autowired
    private LancamentoService service;
    @Autowired
    private ContaCorrenteService contaCorrenteService;

    @Test
    void incluirCredito() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock();
        ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL));
        assertDoesNotThrow(() -> this.service.incluirCredito(lancamentoRequestDTO, contaCorrente));
    }

    @Test
    void incluirCreditoValorNegativo() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(BigDecimal.TEN.negate());
        ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL));
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirCredito(lancamentoRequestDTO, contaCorrente));
    }

    @Test
    void incluirDebito() {
        final LancamentoRequestDTO creditoRequestDTO = super.getLancamentoRequestMock(BigDecimal.valueOf(300));
        ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL));
        assertDoesNotThrow(() -> this.contaCorrenteService.incluirCredito(creditoRequestDTO, contaCorrente.getCodigoUsuario()));

        ContaCorrente contaCorrenteAtualizado = assertDoesNotThrow(() -> this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL));
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(BigDecimal.TEN);
        assertDoesNotThrow(() -> this.service.incluirDebito(lancamentoRequestDTO, contaCorrenteAtualizado));
    }

    @Test
    void incluirDebitoValorNegativo() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(BigDecimal.TEN.negate());
        ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL));
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirDebito(lancamentoRequestDTO, contaCorrente));
    }

    @Test
    void incluirDebitoSaldoInsuficiente() {
        final ContaCorrente contaCorrenteBD = this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL);
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(contaCorrenteBD.getSaldoConta().add(BigDecimal.TEN));
        ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL));
        assertThrows(SaldoInsuficienteException.class, () -> this.service.incluirDebito(lancamentoRequestDTO, contaCorrente));
    }

}
