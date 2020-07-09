package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestTestDTO;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import br.com.cepp.maps.financas.service.AtivoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static br.com.cepp.maps.financas.config.AppDataConfig.CODIGO_USUARIO_GLOBAL;
import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.HEADER_CODIGO_USUARIO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MovimentacaoResourceTest extends AbstractResourceTest {
    private static final String URI_V1 = "/movimentacao/";
    public static final String END_POINT_COMPRA = "compra";
    public static final String END_POINT_VENDA = "venda";

    @Autowired
    private AtivoService ativoService;

    @Test
    void compra() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = this.ativoService.incluir(ativoRequestDTO);
        assertNotNull(ativo);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(ativo.getCodigo());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    @Test
    void compraSaldoInsuficiente() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = this.ativoService.incluir(ativoRequestDTO);
        assertNotNull(ativo);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(ativo.getCodigo());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new SaldoInsuficienteException().getMessage()))
                .andReturn());
    }

    @Test
    void venda() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = this.ativoService.incluir(ativoRequestDTO);
        assertNotNull(ativo);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(ativo.getCodigo());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    @Test
    void consulta() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = this.ativoService.incluir(ativoRequestDTO);
        assertNotNull(ativo);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(ativo.getCodigo());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/ativo"))
                .queryParam("ativo", ativo.getCodigo())
                .queryParam("dataPosicao", movimentoRequestTestDTO.getData())
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn());
    }

    @Test
    void consultaEstoqueNaoExiste() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = this.ativoService.incluir(ativoRequestDTO);
        assertNotNull(ativo);

        final String localDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE);

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/ativo"))
                .queryParam("ativo", ativo.getCodigo())
                .queryParam("dataPosicao", localDate)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }


    @Test
    void consultaPorData() {
        final AtivoRequestDTO ativoRequestDTO = super.getAtivoRequestDTO();
        final Ativo ativo = this.ativoService.incluir(ativoRequestDTO);
        assertNotNull(ativo);

        final LocalDate dataPosicao = LocalDate.now();
        final MovimentoRequestTestDTO movimentoVendaRV = super.getMovimentoRequestTestDTOMock(ativo.getCodigo(), dataPosicao);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoVendaRV.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final MovimentoRequestTestDTO movimentoCompraRV = super.getMovimentoRequestTestDTOMock(ativo.getCodigo(), dataPosicao, 1000);
        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoCompraRV.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final AtivoRequestDTO ativoFundoDTO = super.getAtivoRequestDTO(TipoAtivo.FUNDO);
        final Ativo ativoFundo = this.ativoService.incluir(ativoFundoDTO);
        assertNotNull(ativoFundo);
        final MovimentoRequestTestDTO movimentoVendaFundo = super.getMovimentoRequestTestDTOMock(ativoFundo.getCodigo(), dataPosicao);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoVendaFundo.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final MovimentoRequestTestDTO movimentoCompraFundo = super.getMovimentoRequestTestDTOMock(ativoFundo.getCodigo(), dataPosicao, 1000);
        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoCompraFundo.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final AtivoRequestDTO ativoRFDTO = super.getAtivoRequestDTO(TipoAtivo.RF);
        final Ativo ativoRF = this.ativoService.incluir(ativoRFDTO);
        assertNotNull(ativoRF);
        final MovimentoRequestTestDTO movimentoRF = super.getMovimentoRequestTestDTOMock(ativoRF.getCodigo(), dataPosicao);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRF.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final MovimentoRequestTestDTO movimentoCompraRF = super.getMovimentoRequestTestDTOMock(ativoRF.getCodigo(), dataPosicao, 1000);
        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoCompraRF.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        MvcResult result = assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1)
                .queryParam("dataPosicao", movimentoCompraRF.getData())
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn());

        Object[] response = assertDoesNotThrow(() -> new ObjectMapper().readValue(result.getResponse().getContentAsString(), Object[].class));
        assertNotNull(response);
        assertEquals(3, response.length);
    }

    @Test
    void consultaPorDataEstoqueNaoExiste() {
        final String localDate = LocalDate.now().plusDays(-1L).format(DateTimeFormatter.ISO_DATE);

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1)
                .queryParam("dataPosicao", localDate)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }
}
