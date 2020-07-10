package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.AbstractDataTest;
import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.AtivoValor;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.AtivoValorRequestDTO;
import br.com.cepp.maps.financas.resource.handler.AtivoNaoEncontradoException;
import br.com.cepp.maps.financas.resource.handler.AtivoValorJaExisteException;
import br.com.cepp.maps.financas.resource.handler.AtivoValorNaoEncontradoException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AtivoValorServiceTest extends AbstractDataTest {

    @Autowired
    private AtivoValorService service;
    @Autowired
    private AtivoService ativoService;

    @Test
    void incluir() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = assertDoesNotThrow(() -> this.ativoService.incluir(ativoRequestDTO));
        assertNotNull(ativo);
        assertFalse(Strings.isEmpty(ativo.getCodigo()));
        final AtivoValorRequestDTO ativoValorRequestDTO = super.getAtivoValorRequestDTOMock(ativo.getCodigo());
        final AtivoValor ativoValor = assertDoesNotThrow(() -> this.service.incluir(ativoValorRequestDTO));
        assertNotNull(ativoValor);
        assertEquals(ativo, ativoValor.getAtivo());

        final AtivoValorRequestDTO ativoValorDataFuturaDTO = super.getAtivoValorRequestDTOMock(ativo.getCodigo(), LocalDate.now().plusDays(2));
        final AtivoValor ativoValorDataFutura = assertDoesNotThrow(() -> this.service.incluir(ativoValorDataFuturaDTO));
        assertNotNull(ativoValorDataFutura);
        assertEquals(ativo, ativoValorDataFutura.getAtivo());
    }

    @Test
    void incluirAtivoValorJaExiste() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = assertDoesNotThrow(() -> this.ativoService.incluir(ativoRequestDTO));
        assertNotNull(ativo);
        assertFalse(Strings.isEmpty(ativo.getCodigo()));
        final AtivoValorRequestDTO ativoValorRequestDTO = super.getAtivoValorRequestDTOMock(ativo.getCodigo());
        final AtivoValor ativoValor = assertDoesNotThrow(() -> this.service.incluir(ativoValorRequestDTO));
        assertNotNull(ativoValor);
        assertEquals(ativo, ativoValor.getAtivo());

        assertThrows(AtivoValorJaExisteException.class, () -> this.service.incluir(ativoValorRequestDTO));
    }

    @Test
    void incluirAtivoNaoExiste() {
        final AtivoValorRequestDTO ativoValorRequestDTO = super.getAtivoValorRequestDTOMock(UUID.randomUUID().toString());
        assertThrows(AtivoNaoEncontradoException.class, () -> this.service.incluir(ativoValorRequestDTO));
    }

    @Test
    void incluirValidarParametros() {
        final AtivoValorRequestDTO codigoAtivoNulo = new AtivoValorRequestDTO(null, LocalDate.now(), BigDecimal.TEN);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(codigoAtivoNulo));
        final AtivoValorRequestDTO codigoAtivoVazio = new AtivoValorRequestDTO(Strings.EMPTY, LocalDate.now(), BigDecimal.TEN);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(codigoAtivoVazio));
        final AtivoValorRequestDTO dataVazio = new AtivoValorRequestDTO(RandomStringUtils.random(8, true, true), null, BigDecimal.TEN);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(dataVazio));
        final AtivoValorRequestDTO valorNulo = new AtivoValorRequestDTO(RandomStringUtils.random(8, true, true), LocalDate.now(), null);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(valorNulo));
        final AtivoValorRequestDTO valorInvalido = new AtivoValorRequestDTO(RandomStringUtils.random(8, true, true), LocalDate.now(), BigDecimal.valueOf(999999999));
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(valorInvalido));
    }

    @Test
    void remover() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = assertDoesNotThrow(() -> this.ativoService.incluir(ativoRequestDTO));
        assertNotNull(ativo);
        assertFalse(Strings.isEmpty(ativo.getCodigo()));
        final AtivoValorRequestDTO ativoValorRequestDTO = super.getAtivoValorRequestDTOMock(ativo.getCodigo());
        final AtivoValor ativoValor = assertDoesNotThrow(() -> this.service.incluir(ativoValorRequestDTO));
        assertNotNull(ativoValor);
        assertEquals(ativo, ativoValor.getAtivo());

        assertDoesNotThrow(() -> this.service.remover(ativoRequestDTO.getCodigo(), ativoValor.getData()));

        final String codigoAtivo = ativoValor.getAtivo().getCodigo();
        final LocalDate data = ativoValor.getData();
        assertThrows(AtivoValorNaoEncontradoException.class, () -> this.service.buscarPorAtivoEData(codigoAtivo,
                data));

    }

    @Test
    void removerAtivoValorNaoEncontrado() {
        final LocalDate data = LocalDate.now();
        final String codigo = UUID.randomUUID().toString();
        assertThrows(AtivoValorNaoEncontradoException.class, () -> this.service.remover(codigo, data));
    }

    @Test
    void removerValidarParametros() {
        final LocalDate data = LocalDate.now();
        final String codigo = RandomStringUtils.random(8, true, true);

        assertThrows(ConstraintViolationException.class, () -> this.service.remover(null, data));
        assertThrows(ConstraintViolationException.class, () -> this.service.remover(Strings.EMPTY, data));
        assertThrows(ConstraintViolationException.class, () -> this.service.remover(codigo, null));
    }
}
