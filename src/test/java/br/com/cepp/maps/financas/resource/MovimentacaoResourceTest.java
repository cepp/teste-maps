package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestTestDTO;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import br.com.cepp.maps.financas.service.AtivoService;
import org.apache.commons.lang3.RandomStringUtils;
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

import static br.com.cepp.maps.financas.config.AplicacaoConfig.CODIGO_USUARIO_GLOBAL;
import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.HEADER_CODIGO_USUARIO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
        final String codigo = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, this.getDataDiaUtil());

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo);

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
        final String codigo = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, this.getDataDiaUtil());

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo);

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
        final String codigo = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, this.getDataDiaUtil());

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo);

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
        final String codigo = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, this.getDataDiaUtil());

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo);

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

        final String dataInicio = super.getDataDiaUtil().minusDays(1).format(DateTimeFormatter.ISO_DATE);
        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/{dataInicio}/{dataFim}"), dataInicio, movimentoRequestTestDTO.getData())
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
        final String codigo = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, this.getDataDiaUtil());

        final String dataInicio = super.getDataDiaUtil().minusDays(7).format(DateTimeFormatter.ISO_DATE);
        final String dataFim = super.getDataDiaUtil().minusDays(6).format(DateTimeFormatter.ISO_DATE);

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/{dataInicio}/{dataFim}"), dataInicio, dataFim)
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
        final String codigo = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RF, this.getDataDiaUtil());

        final LocalDate dataPosicao = super.getDataDiaUtil();
        final MovimentoRequestTestDTO movimentoVendaRV = super.getMovimentoRequestTestDTOMock(codigo, dataPosicao);

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

        final MovimentoRequestTestDTO movimentoCompraRV = super.getMovimentoRequestTestDTOMock(codigo, dataPosicao, 1000);
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

        final String codigoFundo = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigoFundo, TipoAtivo.FUNDO, this.getDataDiaUtil());

        final MovimentoRequestTestDTO movimentoVendaFundo = super.getMovimentoRequestTestDTOMock(codigoFundo, dataPosicao);

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

        final MovimentoRequestTestDTO movimentoCompraFundo = super.getMovimentoRequestTestDTOMock(codigoFundo, dataPosicao, 1000);
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

        final String codigoRF = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigoRF, TipoAtivo.RF, this.getDataDiaUtil());
        final MovimentoRequestTestDTO movimentoRF = super.getMovimentoRequestTestDTOMock(codigoRF, dataPosicao);

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

        final MovimentoRequestTestDTO movimentoCompraRF = super.getMovimentoRequestTestDTOMock(codigoRF, dataPosicao, 1000);
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

        MvcResult result = assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/posicao/{dataPosicao}"), dataPosicao.format(DateTimeFormatter.ISO_DATE))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andReturn());
    }

    @Test
    void consultaPorDataEstoqueNaoExiste() {
        final String localDate = super.getDataDiaUtil().minusDays(1L).format(DateTimeFormatter.ISO_DATE);

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/posicao/{dataPosicao}"), localDate)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }
}
