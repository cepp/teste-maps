package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.AtivoValorRequestTestDTO;
import br.com.cepp.maps.financas.service.AtivoService;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AtivoValorResourceTest extends AbstractResourceTest {
    private static final String URI_V1 = "/valorativo/";

    @Autowired
    private AtivoService ativoService;

    @Test
    void incluir() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = assertDoesNotThrow(() -> this.ativoService.incluir(ativoRequestDTO));
        assertNotNull(ativo);
        assertFalse(Strings.isEmpty(ativo.getCodigo()));

        final AtivoValorRequestTestDTO ativoRequestTestDTO = super.getAtivoValorRequestTestDTOMock(ativo.getCodigo());

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
    void incluirAtivoNaoExiste() {
        final AtivoValorRequestTestDTO ativoRequestTestDTO = super.getAtivoValorRequestTestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }

    @Test
    void incluirAtivoValorJaExiste() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = assertDoesNotThrow(() -> this.ativoService.incluir(ativoRequestDTO));
        assertNotNull(ativo);
        assertFalse(Strings.isEmpty(ativo.getCodigo()));

        final AtivoValorRequestTestDTO ativoRequestTestDTO = super.getAtivoValorRequestTestDTOMock(ativo.getCodigo());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
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
    void remover() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = assertDoesNotThrow(() -> this.ativoService.incluir(ativoRequestDTO));
        assertNotNull(ativo);
        assertFalse(Strings.isEmpty(ativo.getCodigo()));

        final AtivoValorRequestTestDTO ativoRequestTestDTO = super.getAtivoValorRequestTestDTOMock(ativo.getCodigo());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(delete(URI_V1)
                .queryParam("codigoAtivo", ativoRequestTestDTO.getCodigoAtivo())
                .queryParam("data", ativoRequestTestDTO.getData())
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }



    @Test
    void removerAtivoValorNaoExiste() {
        final AtivoValorRequestTestDTO ativoRequestTestDTO = super.getAtivoValorRequestTestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(delete(URI_V1)
                .queryParam("codigoAtivo", ativoRequestTestDTO.getCodigoAtivo())
                .queryParam("data", ativoRequestTestDTO.getData())
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }
}
