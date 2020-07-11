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
import java.time.LocalDate;

import static br.com.cepp.maps.financas.config.AplicacaoConfig.CODIGO_USUARIO_GLOBAL;
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
        assertDoesNotThrow(() -> this.service.incluirCredito(lancamentoRequestDTO, CODIGO_USUARIO_GLOBAL));
    }

    @Test
    void incluirCreditoValorNegativo() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(BigDecimal.TEN.negate());
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirCredito(lancamentoRequestDTO, CODIGO_USUARIO_GLOBAL));
    }

    @Test
    void incluirDebitoValorNegativo() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(BigDecimal.TEN.negate());
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirDebito(lancamentoRequestDTO, CODIGO_USUARIO_GLOBAL));
    }

    @Test
    void incluirDebitoSaldoInsuficiente() {
        final LocalDate data = LocalDate.now();
        final ContaCorrente contaCorrenteBD = this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL, data);
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(contaCorrenteBD.getSaldoConta().add(BigDecimal.TEN));
        assertThrows(SaldoInsuficienteException.class, () -> this.service.incluirDebito(lancamentoRequestDTO, CODIGO_USUARIO_GLOBAL));
    }

}
