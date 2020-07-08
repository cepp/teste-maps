package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.AbstractDataTest;
import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.AtivoJaExisteException;
import br.com.cepp.maps.financas.resource.handler.AtivoNaoEncontradoException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class AtivoServiceTest extends AbstractDataTest {

    @Autowired
    private AtivoService service;

    @Test
    void incluir() {
        AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        assertDoesNotThrow(() -> this.service.incluir(ativoRequestDTO));
    }

    @Test
    void incluirValidarRequest() {
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(null));

        final AtivoRequestDTO ativoRequestInvalido = new AtivoRequestDTO(null, null, null, null);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(ativoRequestInvalido));

        final AtivoRequestDTO ativoRequestCodigoNulo = new AtivoRequestDTO(null, BigDecimal.TEN,
                RandomStringUtils.random(8, true, true), TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(ativoRequestCodigoNulo));

        final AtivoRequestDTO ativoRequestCodigoVazio = new AtivoRequestDTO(Strings.EMPTY, BigDecimal.TEN,
                RandomStringUtils.random(8, true, true), TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(ativoRequestCodigoVazio));

        final AtivoRequestDTO ativoRequestValorNulo = new AtivoRequestDTO(RandomStringUtils.random(8, true, true),
                null, RandomStringUtils.random(8, true, true), TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(ativoRequestValorNulo));

        BigDecimal valorDecimal = BigDecimal.valueOf(9999999999L, 9);
        final AtivoRequestDTO ativoRequestValorDecimalInvalido = new AtivoRequestDTO(RandomStringUtils.random(8, true, true),
                valorDecimal, RandomStringUtils.random(8, true, true), TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(ativoRequestValorDecimalInvalido));

        BigDecimal valorInteiro = BigDecimal.valueOf(999999999999999999L);
        final AtivoRequestDTO ativoRequestValorInteiroInvalido = new AtivoRequestDTO(RandomStringUtils.random(8, true, true),
                valorInteiro, RandomStringUtils.random(8, true, true), TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(ativoRequestValorInteiroInvalido));

        final AtivoRequestDTO ativoRequestNomeNulo = new AtivoRequestDTO(RandomStringUtils.random(8, true, true),
                BigDecimal.TEN, null, TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(ativoRequestNomeNulo));

        final AtivoRequestDTO ativoRequestNomeVazio = new AtivoRequestDTO(RandomStringUtils.random(8, true, true),
                BigDecimal.TEN, Strings.EMPTY, TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(ativoRequestNomeVazio));

        final AtivoRequestDTO ativoRequestTipoNulo = new AtivoRequestDTO(RandomStringUtils.random(8, true, true),
                BigDecimal.TEN, RandomStringUtils.random(8, true, true), null);
        assertThrows(ConstraintViolationException.class, () -> this.service.incluir(ativoRequestTipoNulo));
    }

    @Test
    void incluirAtivoJaExiste() {
        AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        assertDoesNotThrow(() -> this.service.incluir(ativoRequestDTO));
        assertThrows(AtivoJaExisteException.class, () -> this.service.incluir(ativoRequestDTO));
    }

    @Test
    void alterar() {
        final AtivoRequestDTO incluirAtivoRequestDTO = super.getAtivoRequestDTO();
        assertDoesNotThrow(() -> this.service.incluir(incluirAtivoRequestDTO));
        final AtivoRequestDTO alterarAtivoRequestDTO = super.getAtivoRequestDTO(incluirAtivoRequestDTO.getCodigo());
        assertDoesNotThrow(() -> this.service.alterar(alterarAtivoRequestDTO, incluirAtivoRequestDTO.getCodigo()));
    }

    @Test
    void alterarValidarParametros() {
        final AtivoRequestDTO alterarAtivoRequestDTO = super.getAtivoRequestDTO();
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(alterarAtivoRequestDTO, null));
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(alterarAtivoRequestDTO, Strings.EMPTY));

        assertNotNull(alterarAtivoRequestDTO);
        assertNotNull(alterarAtivoRequestDTO.getCodigo());
        final String codigo = alterarAtivoRequestDTO.getCodigo();

        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(null, codigo));

        final AtivoRequestDTO ativoRequestInvalido = new AtivoRequestDTO(null, null, null, null);
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(ativoRequestInvalido, codigo));

        final AtivoRequestDTO ativoRequestCodigoNulo = new AtivoRequestDTO(null, BigDecimal.TEN, RandomStringUtils.random(8, true, true), TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(ativoRequestCodigoNulo, codigo));

        final AtivoRequestDTO ativoRequestCodigoVazio = new AtivoRequestDTO(Strings.EMPTY, BigDecimal.TEN, RandomStringUtils.random(8, true, true), TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(ativoRequestCodigoVazio, codigo));

        final AtivoRequestDTO ativoRequestValorNulo = new AtivoRequestDTO(RandomStringUtils.random(8, true, true), null, RandomStringUtils.random(8, true, true), TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(ativoRequestValorNulo, codigo));

        BigDecimal valorDecimal = BigDecimal.valueOf(9999999999L, 9);
        final AtivoRequestDTO ativoRequestValorDecimalInvalido = new AtivoRequestDTO(RandomStringUtils.random(8, true, true), valorDecimal, RandomStringUtils.random(8, true, true), TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(ativoRequestValorDecimalInvalido, codigo));

        BigDecimal valorInteiro = BigDecimal.valueOf(999999999999999999L);
        final AtivoRequestDTO ativoRequestValorInteiroInvalido = new AtivoRequestDTO(RandomStringUtils.random(8, true, true), valorInteiro, RandomStringUtils.random(8, true, true), TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(ativoRequestValorInteiroInvalido, codigo));

        final AtivoRequestDTO ativoRequestNomeNulo = new AtivoRequestDTO(RandomStringUtils.random(8, true, true), BigDecimal.TEN, null, TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(ativoRequestNomeNulo, codigo));

        final AtivoRequestDTO ativoRequestNomeVazio = new AtivoRequestDTO(RandomStringUtils.random(8, true, true), BigDecimal.TEN, Strings.EMPTY, TipoAtivo.RV);
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(ativoRequestNomeVazio, codigo));

        final AtivoRequestDTO ativoRequestTipoNulo = new AtivoRequestDTO(RandomStringUtils.random(8, true, true), BigDecimal.TEN, RandomStringUtils.random(8, true, true), null);
        assertThrows(ConstraintViolationException.class, () -> this.service.alterar(ativoRequestTipoNulo, codigo));
    }

    @Test
    void alterarAtivoNaoExiste() {
        AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final String codigo = ativoRequestDTO.getCodigo();
        assertThrows(AtivoNaoEncontradoException.class, () -> this.service.alterar(ativoRequestDTO, codigo));
    }

    @Test
    void remover() {
        final AtivoRequestDTO incluirAtivoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = assertDoesNotThrow(() -> this.service.incluir(incluirAtivoRequestDTO));
        assertNotNull(ativo);
        assertDoesNotThrow(() -> this.service.remover(ativo.getCodigo()));
    }

    @Test
    void removerValidarParametro() {
        assertThrows(ConstraintViolationException.class, () -> this.service.remover(null));
        assertThrows(ConstraintViolationException.class, () -> this.service.remover(Strings.EMPTY));
    }

    @Test
    void removerAtivoNaoExiste() {
        AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final String codigo = ativoRequestDTO.getCodigo();
        assertThrows(AtivoNaoEncontradoException.class, () -> this.service.remover(codigo));
    }

    @Test
    void buscarPorCodigo() {
        final AtivoRequestDTO incluirAtivoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = assertDoesNotThrow(() -> this.service.incluir(incluirAtivoRequestDTO));
        assertNotNull(ativo);

        final Ativo ativoBD = assertDoesNotThrow(() -> this.service.buscarPorCodigo(ativo.getCodigo()));
        assertNotNull(ativoBD);
        assertEquals(ativo, ativoBD);
    }

    @Test
    void buscarPorCodigoValidarParametros() {
        assertThrows(ConstraintViolationException.class, () -> this.service.buscarPorCodigo(null));
        assertThrows(ConstraintViolationException.class, () -> this.service.buscarPorCodigo(Strings.EMPTY));
    }

    @Test
    void buscarPorCodigoAtivoNaoExiste() {
        final String codigo = RandomStringUtils.random(10, true, true);
        assertThrows(AtivoNaoEncontradoException.class, () -> this.service.remover(codigo));
    }
}
