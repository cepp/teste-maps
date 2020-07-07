package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.AbstractDataTest;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

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
        this.contaCorrenteService.incluirContaCorrente(lancamentoRequestDTO.getCodigoUsuario());
        assertDoesNotThrow(() -> this.service.incluirCredito(lancamentoRequestDTO));
    }

    @Test
    void incluirCreditoValorNegativo() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(BigDecimal.TEN.negate());
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirCredito(lancamentoRequestDTO));
    }

    @Test
    void incluirDebito() {
        final LancamentoRequestDTO creditoRequestDTO = super.getLancamentoRequestMock(BigDecimal.valueOf(300));
        this.contaCorrenteService.incluirContaCorrente(creditoRequestDTO.getCodigoUsuario());
        assertDoesNotThrow(() -> this.service.incluirCredito(creditoRequestDTO));

        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(BigDecimal.TEN, creditoRequestDTO.getCodigoUsuario());
        assertDoesNotThrow(() -> this.service.incluirDebito(lancamentoRequestDTO));
    }

    @Test
    void incluirDebitoValorNegativo() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(BigDecimal.TEN.negate());
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirDebito(lancamentoRequestDTO));
    }

    @Test
    void incluirDebitoSaldoInsuficiente() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock();
        assertThrows(SaldoInsuficienteException.class, () -> this.service.incluirDebito(lancamentoRequestDTO));
    }

}
