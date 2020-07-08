package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.AbstractDataTest;
import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.ContaNaoEncontradaException;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.cepp.maps.financas.config.AppDataConfig.CODIGO_USUARIO_GLOBAL;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ContaCorrenteServiceTest extends AbstractDataTest {

    @Autowired
    private ContaCorrenteService service;

    @Test
    void buscarContaCorrentePorCodigoUsuario() {
        ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL));
        assertNotNull(contaCorrente);
    }

    @Test
    void buscarContaCorrentePorCodigoUsuarioNaoExiste() {
        final String usuario = RandomStringUtils.random(10, true, false);
        assertThrows(ContaNaoEncontradaException.class, () -> this.service.buscarContaCorrentePorCodigoUsuario(usuario));
    }

    @Test
    void buscarContaCorrentePorCodigoUsuarioValidaCodigoUsuario() {
        assertThrows(ConstraintViolationException.class, () -> this.service.buscarContaCorrentePorCodigoUsuario(null));
        assertThrows(ConstraintViolationException.class, () -> this.service.buscarContaCorrentePorCodigoUsuario(Strings.EMPTY));
    }

    @Test
    void atualizarSaldo() {
        final ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL));
        assertNotNull(contaCorrente);

        final ContaCorrente contaCorrenteAtualizada = assertDoesNotThrow(() -> this.service.atualizarSaldo(contaCorrente,
                BigDecimal.TEN, TipoNatureza.CREDITO));
        assertNotNull(contaCorrenteAtualizada);
        assertTrue(contaCorrenteAtualizada.getSaldoConta().compareTo(contaCorrente.getSaldoConta()) > 0);
        assertEquals(contaCorrenteAtualizada.getSaldoConta(), contaCorrente.getSaldoConta().add(BigDecimal.TEN));
    }

    @Test
    void atualizarSaldoValidarContaCorrente() {
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldo(null,
                BigDecimal.TEN, TipoNatureza.CREDITO));

        final ContaCorrente contaCorrenteInvalida = new ContaCorrente(null, null, null);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldo(contaCorrenteInvalida,
                BigDecimal.TEN, TipoNatureza.CREDITO));

        final ContaCorrente contaCorrenteSaldoNulo = new ContaCorrente(null, null, CODIGO_USUARIO_GLOBAL);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldo(contaCorrenteSaldoNulo,
                BigDecimal.TEN, TipoNatureza.CREDITO));

        final ContaCorrente contaCorrenteSaldoInvalido = new ContaCorrente(null, BigDecimal.TEN.negate(), null);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldo(contaCorrenteSaldoInvalido,
                BigDecimal.TEN, TipoNatureza.CREDITO));

        final ContaCorrente contaCorrenteUsuarioNulo = new ContaCorrente(null, BigDecimal.TEN, null);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldo(contaCorrenteUsuarioNulo,
                BigDecimal.TEN, TipoNatureza.CREDITO));

        final ContaCorrente contaCorrenteUsuarioVazio = new ContaCorrente(null, BigDecimal.TEN, Strings.EMPTY);
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldo(contaCorrenteUsuarioVazio,
                BigDecimal.TEN, TipoNatureza.CREDITO));
    }

    @Test
    void atualizarSaldoValidarSaldoInsuficiente() {
        final ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL));
        assertNotNull(contaCorrente);

        assertThrows(SaldoInsuficienteException.class, () -> this.service.atualizarSaldo(contaCorrente,
                BigDecimal.valueOf(20000000), TipoNatureza.DEBITO));
    }

    @Test
    void incluirContaCorrente() {
        final String usuario = RandomStringUtils.random(30, true, true);
        final ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.service.incluirContaCorrente(usuario));
        assertNotNull(contaCorrente);

        final ContaCorrente contaCorrenteBD = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(usuario));
        assertNotNull(contaCorrenteBD);

        assertEquals(contaCorrente, contaCorrenteBD);
    }

    @Test
    void incluirContaCorrenteValidarCodigoUsuario() {
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirContaCorrente(null));
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirContaCorrente(Strings.EMPTY));
    }

    @Test
    void incluirCredito() {
        LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock();
        assertDoesNotThrow(() -> this.service.incluirCredito(lancamentoRequestDTO, CODIGO_USUARIO_GLOBAL));
    }

    @Test
    void incluirCreditoValidarRequestDTO() {
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirCredito(null, CODIGO_USUARIO_GLOBAL));

        final LancamentoRequestDTO requestDTOValorInvalido = new LancamentoRequestDTO(null,
                RandomStringUtils.random(10, true, true), LocalDate.now());
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirCredito(requestDTOValorInvalido, CODIGO_USUARIO_GLOBAL));

        final LancamentoRequestDTO requestDTODescricaoNula = new LancamentoRequestDTO(null,
                null, LocalDate.now());
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirCredito(requestDTODescricaoNula, CODIGO_USUARIO_GLOBAL));

        final LancamentoRequestDTO requestDTODescricaoVazia = new LancamentoRequestDTO(null,
                Strings.EMPTY, LocalDate.now());
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirCredito(requestDTODescricaoVazia, CODIGO_USUARIO_GLOBAL));

        final LancamentoRequestDTO requestDTODataNula = new LancamentoRequestDTO(null,
                RandomStringUtils.random(10, true, true), null);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirCredito(requestDTODataNula, CODIGO_USUARIO_GLOBAL));
    }

    @Test
    void incluirCreditoContaNaoExiste() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock();
        final String usuario = RandomStringUtils.random(10, true, true);
        assertThrows(ContaNaoEncontradaException.class, () -> this.service.incluirCredito(lancamentoRequestDTO, usuario));
    }

    @Test
    void incluirDebito() {
        LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(BigDecimal.ONE);
        assertDoesNotThrow(() -> this.service.incluirDebito(lancamentoRequestDTO, CODIGO_USUARIO_GLOBAL));
    }

    @Test
    void incluirDebitoValidarRequestDTO() {
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirDebito(null, CODIGO_USUARIO_GLOBAL));

        final LancamentoRequestDTO requestDTOValorInvalido = new LancamentoRequestDTO(null,
                RandomStringUtils.random(10, true, true), LocalDate.now());
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirDebito(requestDTOValorInvalido, CODIGO_USUARIO_GLOBAL));

        final LancamentoRequestDTO requestDTODescricaoNula = new LancamentoRequestDTO(null,
                null, LocalDate.now());
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirDebito(requestDTODescricaoNula, CODIGO_USUARIO_GLOBAL));

        final LancamentoRequestDTO requestDTODescricaoVazia = new LancamentoRequestDTO(null,
                Strings.EMPTY, LocalDate.now());
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirDebito(requestDTODescricaoVazia, CODIGO_USUARIO_GLOBAL));

        final LancamentoRequestDTO requestDTODataNula = new LancamentoRequestDTO(null,
                RandomStringUtils.random(10, true, true), null);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirDebito(requestDTODataNula, CODIGO_USUARIO_GLOBAL));
    }

    @Test
    void incluirDebitoContaNaoExiste() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock();
        final String usuario = RandomStringUtils.random(10, true, true);
        assertThrows(ContaNaoEncontradaException.class, () -> this.service.incluirDebito(lancamentoRequestDTO, usuario));
    }

    @Test
    void incluirDebitoSaldoInsuficiente() {
        final LancamentoRequestDTO lancamentoRequestDTO = super.getLancamentoRequestMock(BigDecimal.valueOf(60000000));
        assertThrows(SaldoInsuficienteException.class, () -> this.service.incluirDebito(lancamentoRequestDTO, CODIGO_USUARIO_GLOBAL));
    }
}
