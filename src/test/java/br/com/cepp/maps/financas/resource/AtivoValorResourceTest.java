package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.AtivoValorRequestTestDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestTestDTO;
import br.com.cepp.maps.financas.service.AtivoService;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

import static br.com.cepp.maps.financas.config.AplicacaoConfig.CODIGO_USUARIO_GLOBAL;
import static br.com.cepp.maps.financas.config.MockMvcConfig.UTF_8;
import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.HEADER_CODIGO_USUARIO;
import static br.com.cepp.maps.financas.resource.MovimentacaoResourceTest.END_POINT_VENDA;
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
    @WithMockUser(authorities={"ROLE_ADMIN"})
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
    @WithMockUser(authorities={"ROLE_ADMIN"})
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
    @WithMockUser(authorities={"ROLE_ADMIN"})
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
    @WithMockUser(authorities={"ROLE_ADMIN"})
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

        assertDoesNotThrow(() -> super.getMockMvc().perform(delete(URI_V1.concat("/{codigoAtivo}/{data}"),
                ativoRequestTestDTO.getCodigoAtivo(),
                ativoRequestTestDTO.getData())
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_ADMIN"})
    void removerAtivoValorNaoExiste() {
        final AtivoValorRequestTestDTO ativoRequestTestDTO = super.getAtivoValorRequestTestDTOMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(delete(URI_V1.concat("/{codigoAtivo}/{data}"),
                ativoRequestTestDTO.getCodigoAtivo(),
                ativoRequestTestDTO.getData())
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_ADMIN", "ROLE_USER"})
    void removerAtivoValorUtilizado() {
        final LocalDate dataEmissao = super.getDataDiaUtil();
        final LocalDate dataVencimento = dataEmissao.plusDays(5);
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO(dataEmissao, dataVencimento);
        final Ativo ativo = assertDoesNotThrow(() -> this.ativoService.incluir(ativoRequestDTO));
        assertNotNull(ativo);
        assertFalse(Strings.isEmpty(ativo.getCodigo()));

        final LocalDate dataPosicao = super.getDataDiaUtil();
        final AtivoValorRequestTestDTO ativoRequestTestDTO = super.getAtivoValorRequestTestDTOMock(ativo.getCodigo(),
                dataPosicao);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(ativoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final double valor = Double.parseDouble(ativoRequestTestDTO.getValor());
        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(ativo.getCodigo(),
                dataPosicao, BigDecimal.valueOf(valor).setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(MovimentacaoResourceTest.URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(delete(URI_V1.concat("/{codigoAtivo}/{data}"),
                ativoRequestTestDTO.getCodigoAtivo(),
                ativoRequestTestDTO.getData())
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isConflict())
                .andReturn());
    }
}
