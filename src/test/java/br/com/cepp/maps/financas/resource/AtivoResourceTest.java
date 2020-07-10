package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.resource.dto.AtivoRequestTestDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AtivoResourceTest extends AbstractResourceTest {
    private static final String URI_V1 = "/ativos/";

    @Test
    void incluir() {
        final AtivoRequestTestDTO ativoRequestTestDTO = super.getAtivoRequestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    @Test
    void incluirValidarRequest() {
        final AtivoRequestTestDTO validarCodigo = super.getAtivoRequestDTOMock();
        validarCodigo.setCodigo(null);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validarCodigo.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'codigo' é obrigatório]"))
                .andReturn());

        validarCodigo.setCodigo(Strings.EMPTY);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validarCodigo.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'codigo' é obrigatório]"))
                .andReturn());

        final AtivoRequestTestDTO validarNome = super.getAtivoRequestDTOMock();
        validarNome.setNome(null);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validarNome.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'nome' é obrigatório]"))
                .andReturn());

        validarNome.setNome(Strings.EMPTY);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validarNome.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'nome' é obrigatório]"))
                .andReturn());

        final AtivoRequestTestDTO validarTipo = super.getAtivoRequestDTOMock();
        validarTipo.setTipoAtivo(null);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validarTipo.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'tipoAtivo' é obrigatório]"))
                .andReturn());

        validarTipo.setTipoAtivo(RandomStringUtils.random(7, true, true));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validarTipo.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());

        final AtivoRequestTestDTO validarDataEmissaoMaiorDataVencimento = super.getAtivoRequestDTOMock();
        validarDataEmissaoMaiorDataVencimento.setDataEmissao(LocalDate.now().plusDays(3).format(DateTimeFormatter.ISO_DATE));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(validarDataEmissaoMaiorDataVencimento.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Data Emissão deve ser sempre anterior à Data Vencimento"))
                .andReturn());
    }

    @Test
    void incluirAtivoJaExiste() {
        final AtivoRequestTestDTO ativoRequestTestDTO = super.getAtivoRequestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andReturn());
    }

    @Test
    void alterar() {
        final AtivoRequestTestDTO ativoRequestTestDTO = super.getAtivoRequestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        ativoRequestTestDTO.setNome(RandomStringUtils.random(20, true, true));

        assertDoesNotThrow(() -> super.getMockMvc().perform(put(URI_V1.concat(ativoRequestTestDTO.getCodigo()))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    @Test
    void alterarAtivoNaoExiste() {
        final AtivoRequestTestDTO ativoRequestTestDTO = super.getAtivoRequestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(put(URI_V1.concat(ativoRequestTestDTO.getCodigo()))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }

    @Test
    void alterarValidarPathParam() {
        final AtivoRequestTestDTO ativoRequestTestDTO = super.getAtivoRequestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(put(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isMethodNotAllowed())
                .andReturn());
    }

    @Test
    void remover() {
        final AtivoRequestTestDTO ativoRequestTestDTO = super.getAtivoRequestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(delete(URI_V1.concat(ativoRequestTestDTO.getCodigo()))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    @Test
    void removerAtivoNaoExiste() {
        final AtivoRequestTestDTO ativoRequestTestDTO = super.getAtivoRequestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(delete(URI_V1.concat(ativoRequestTestDTO.getCodigo()))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }

    @Test
    void removerValidarPathParam() {
        assertDoesNotThrow(() -> super.getMockMvc().perform(delete(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isMethodNotAllowed())
                .andReturn());
    }

    @Test
    void consultaPorCodigo() {
        final AtivoRequestTestDTO ativoRequestTestDTO = super.getAtivoRequestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat(ativoRequestTestDTO.getCodigo()))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn());
    }

    @Test
    void consultaPorCodigoAtivoNaoExiste() {
        final AtivoRequestTestDTO ativoRequestTestDTO = super.getAtivoRequestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat(ativoRequestTestDTO.getCodigo()))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }

    @Test
    void consultaPorCodigoValidarPathParam() {
        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isMethodNotAllowed())
                .andReturn());
    }
}
