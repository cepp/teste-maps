package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.AbstractDataTest;
import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import br.com.cepp.maps.financas.resource.handler.ContaNaoEncontradaException;
import br.com.cepp.maps.financas.resource.handler.ContaPosicaoJaExisteException;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static br.com.cepp.maps.financas.config.AplicacaoConfig.CODIGO_USUARIO_GLOBAL;
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
        final LocalDate data = LocalDate.now();
        if(!this.service.existePosicaoPorUsuarioData(CODIGO_USUARIO_GLOBAL, data)) {
            final ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.service.incluirPosicaoContaCorrente(CODIGO_USUARIO_GLOBAL, data));
            assertNotNull(contaCorrente);
        }

        ContaCorrente contaCorrenteBD = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL, data));
        assertNotNull(contaCorrenteBD);
    }

    @Test
    void buscarContaCorrentePorCodigoUsuarioNaoExiste() {
        final String usuario = RandomStringUtils.random(10, true, false);
        final LocalDate data = LocalDate.now();
        assertThrows(ContaNaoEncontradaException.class, () -> this.service.buscarContaCorrentePorCodigoUsuario(usuario, data));
    }

    @Test
    void buscarContaCorrentePorCodigoUsuarioValidaCodigoUsuario() {
        final LocalDate data = LocalDate.now();
        assertThrows(ConstraintViolationException.class, () -> this.service.buscarContaCorrentePorCodigoUsuario(null, data));
        assertThrows(ConstraintViolationException.class, () -> this.service.buscarContaCorrentePorCodigoUsuario(Strings.EMPTY, data));
        assertThrows(ConstraintViolationException.class, () -> this.service.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL, null));
    }

    @Test
    void atualizarSaldo() {
        final LocalDate data = LocalDate.now();
        final ContaCorrente primeiraAtualizacao = assertDoesNotThrow(() -> this.service.atualizarSaldo(CODIGO_USUARIO_GLOBAL,
                BigDecimal.TEN, TipoNatureza.CREDITO, data));
        assertNotNull(primeiraAtualizacao);

        final ContaCorrente contaCorrenteBD = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL, data));
        assertNotNull(contaCorrenteBD);
        assertEquals(contaCorrenteBD.getData(), primeiraAtualizacao.getData());
        assertEquals(contaCorrenteBD.getSaldoConta(), primeiraAtualizacao.getSaldoConta());

        final ContaCorrente segundaAtualizacao = assertDoesNotThrow(() -> this.service.atualizarSaldo(CODIGO_USUARIO_GLOBAL,
                BigDecimal.TEN, TipoNatureza.CREDITO, data));
        assertNotNull(segundaAtualizacao);
        assertEquals(contaCorrenteBD.getSaldoConta().add(BigDecimal.TEN), segundaAtualizacao.getSaldoConta());
    }

    @Test
    void atualizarSaldoValidarContaCorrente() {
        final LocalDate data = LocalDate.now();
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldo(null,
                BigDecimal.TEN, TipoNatureza.CREDITO, data));

        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldo(CODIGO_USUARIO_GLOBAL,
                null, TipoNatureza.CREDITO, data));

        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldo(CODIGO_USUARIO_GLOBAL,
                BigDecimal.TEN, null, data));

        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldo(CODIGO_USUARIO_GLOBAL,
                BigDecimal.TEN, TipoNatureza.CREDITO, null));
    }

    @Test
    void atualizarSaldoValidarSaldoInsuficiente() {
        final BigDecimal valor = BigDecimal.valueOf(20000000);
        final LocalDate data = LocalDate.now();

        assertThrows(SaldoInsuficienteException.class, () -> this.service.atualizarSaldo(CODIGO_USUARIO_GLOBAL, valor,
                TipoNatureza.DEBITO, data));
    }

    @Test
    void incluirContaCorrente() {
        final String usuario = RandomStringUtils.random(30, true, true);
        final LocalDate data = LocalDate.now();
        final ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.service.incluirPosicaoContaCorrente(usuario, data));
        assertNotNull(contaCorrente);

        final ContaCorrente contaCorrenteBD = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(usuario, data));
        assertNotNull(contaCorrenteBD);
        assertEquals(contaCorrente, contaCorrenteBD);

        assertThrows(ContaPosicaoJaExisteException.class, () -> this.service.incluirPosicaoContaCorrente(usuario, data));
    }

    @Test
    void incluirContaCorrenteDataFutura() {
        final String usuario = RandomStringUtils.random(30, true, true);
        final LocalDate dataPrimeiroLancamento = LocalDate.now().plusDays(2);
        final ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.service.incluirPosicaoContaCorrente(usuario, dataPrimeiroLancamento));
        assertNotNull(contaCorrente);
        assertEquals(BigDecimal.ZERO, contaCorrente.getSaldoConta());

        final ContaCorrente primeiraAtualizacao = assertDoesNotThrow(() -> this.service.atualizarSaldo(usuario,
                BigDecimal.TEN, TipoNatureza.CREDITO, dataPrimeiroLancamento));
        assertNotNull(primeiraAtualizacao);
        assertEquals(BigDecimal.TEN, primeiraAtualizacao.getSaldoConta());

        final ContaCorrente contaCorrenteBD = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(usuario, dataPrimeiroLancamento));
        assertNotNull(contaCorrenteBD);
        assertEquals(primeiraAtualizacao, contaCorrenteBD);
        assertEquals(BigDecimal.TEN, contaCorrenteBD.getSaldoConta());

        final LocalDate dataSegunadoLancamento = dataPrimeiroLancamento.plusDays(2);
        final ContaCorrente segundaAtualizacao = assertDoesNotThrow(() -> this.service.atualizarSaldo(usuario, BigDecimal.TEN,
                TipoNatureza.CREDITO, dataSegunadoLancamento));
        assertNotNull(segundaAtualizacao);
        assertEquals(BigDecimal.TEN.add(BigDecimal.TEN), segundaAtualizacao.getSaldoConta());

        final LocalDate dataPosicaoAtual = LocalDate.now();
        final ContaCorrente posicaoAtual = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(usuario, dataPosicaoAtual));
        assertNotNull(posicaoAtual);
        assertEquals(BigDecimal.ZERO, posicaoAtual.getSaldoConta());

        final ContaCorrente posicaoPrimeiroLancamento = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(usuario, dataPrimeiroLancamento));
        assertNotNull(posicaoPrimeiroLancamento);
        assertEquals(BigDecimal.TEN, posicaoPrimeiroLancamento.getSaldoConta());

        final ContaCorrente posicaoSegundoLancamento = assertDoesNotThrow(() -> this.service.buscarContaCorrentePorCodigoUsuario(usuario, dataSegunadoLancamento));
        assertNotNull(posicaoSegundoLancamento);
        assertEquals(BigDecimal.TEN.add(BigDecimal.TEN), posicaoSegundoLancamento.getSaldoConta());
    }

    @Test
    void incluirContaCorrenteValidarCodigoUsuario() {
        final LocalDate data = LocalDate.now();
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirPosicaoContaCorrente(null, data));
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirPosicaoContaCorrente(Strings.EMPTY, data));
        assertThrows(ConstraintViolationException.class, () -> this.service.incluirPosicaoContaCorrente(CODIGO_USUARIO_GLOBAL, null));
    }

    @Test
    void atualizarSaldoMovimento() {
        final LocalDate data = LocalDate.now();
        final String usuario = RandomStringUtils.random(30, true, true);
        final ContaCorrente contaCorrente = assertDoesNotThrow(() -> this.service.atualizarSaldo(usuario, BigDecimal.TEN,
                TipoNatureza.CREDITO, data));
        assertNotNull(contaCorrente);

        final ContaCorrente contaCorrenteAtualizada = assertDoesNotThrow(() -> this.service.atualizarSaldoMovimento(usuario,
                BigDecimal.TEN, TipoNatureza.CREDITO, data));
        assertNotNull(contaCorrenteAtualizada);
        assertTrue(contaCorrenteAtualizada.getSaldoConta().compareTo(contaCorrente.getSaldoConta()) > 0);
        assertEquals(contaCorrenteAtualizada.getSaldoConta(), contaCorrente.getSaldoConta().add(BigDecimal.TEN));
    }

    @Test
    void atualizarSaldoMovimentoValidarParametros() {
        final LocalDate data = LocalDate.now();

        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldoMovimento(null,
                BigDecimal.TEN, TipoNatureza.CREDITO, data));
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldoMovimento(Strings.EMPTY,
                BigDecimal.TEN, TipoNatureza.CREDITO,data));
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldoMovimento(CODIGO_USUARIO_GLOBAL,
                null, TipoNatureza.CREDITO, data));
        assertThrows(ConstraintViolationException.class, () -> this.service.atualizarSaldoMovimento(CODIGO_USUARIO_GLOBAL,
                BigDecimal.TEN, null, data));
    }
}
