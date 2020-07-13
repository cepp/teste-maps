package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.AtivoValor;
import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestTestDTO;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static br.com.cepp.maps.financas.config.MockMvcConfig.UTF_8;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class MovimentacaoResourceTest extends AbstractResourceTest {
    public static final String URI_V1 = "/movimentacao/";
    public static final String END_POINT_COMPRA = "compra";
    public static final String END_POINT_VENDA = "venda";
    public static final int QUANTIDADE_MAX_DEFAULT = 20000;

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void compra() {
        final String codigo = RandomStringUtils.random(10, true, true);
        final LocalDate dataPosicao = this.getDataDiaUtil();
        final AtivoValor ativoValor = super.iniciarAtivoValor(codigo, TipoAtivo.RV, dataPosicao);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo, dataPosicao,
                QUANTIDADE_MAX_DEFAULT, ativoValor.getValor().setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void compraSaldoInsuficiente() {
        final String codigo = RandomStringUtils.random(10, true, true);
        final LocalDate dataPosicao = super.getDataDiaUtil();
        final AtivoValor ativoValor = super.iniciarAtivoValor(codigo, TipoAtivo.RV, dataPosicao);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo, dataPosicao,
                999999, ativoValor.getValor().setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new SaldoInsuficienteException().getMessage()))
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_ADMIN"})
    void compraPerfilAdmin() {
        final String codigo = RandomStringUtils.random(10, true, true);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn());
    }

    @Test
    void compraNaoAutenticado() {
        final String codigo = RandomStringUtils.random(10, true, true);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void venda() {
        final String codigo = RandomStringUtils.random(10, true, true);
        final LocalDate dataPosicao = super.getDataDiaUtil();
        final AtivoValor ativoValor = super.iniciarAtivoValor(codigo, TipoAtivo.RV, dataPosicao);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo, dataPosicao,
                QUANTIDADE_MAX_DEFAULT, ativoValor.getValor().setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void vendaFinalSemana() {
        final String codigo = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, this.getDataDiaUtil().plusDays(1));

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo,
                this.getDataDiaUtil().plusDays(1));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void vendaAtivoPeriodoInvalido() {
        final String codigo = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, this.getDataDiaUtil().minusDays(3));

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo,
                this.getDataDiaUtil().minusDays(3));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void vendaMovimentoValorDiferente() {
        final String codigo = RandomStringUtils.random(10, true, true);
        final LocalDate dataPosicao = super.getDataDiaUtil();
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, dataPosicao);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo, dataPosicao,
                QUANTIDADE_MAX_DEFAULT, BigDecimal.TEN.setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_ADMIN"})
    void vendaPerfilAdmin() {
        final String codigo = RandomStringUtils.random(10, true, true);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn());
    }

    @Test
    void vendaNaoAutenticado() {
        final String codigo = RandomStringUtils.random(10, true, true);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void consulta() {
        final String codigo = RandomStringUtils.random(10, true, true);
        final LocalDate dataInicio = this.getDataDiaUtil().minusDays(4);
        final LocalDate dataFim = this.getDataDiaUtil().minusDays(1);
        final AtivoValor ativoValor = super.iniciarAtivoValor(codigo, TipoAtivo.RV, dataInicio, dataInicio, dataFim);

        final MovimentoRequestTestDTO movimentoRequestTestDTO = super.getMovimentoRequestTestDTOMock(codigo, dataInicio,
                QUANTIDADE_MAX_DEFAULT, ativoValor.getValor().setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final LocalDate dataPosicao = dataInicio.plusDays(2);
        final AtivoValor ativoValorNovaData = super.iniciarAtivoValor(codigo, TipoAtivo.RV, dataPosicao);
        final MovimentoRequestTestDTO requestNovaData = super.getMovimentoRequestTestDTOMock(codigo, dataPosicao,
                QUANTIDADE_MAX_DEFAULT, ativoValorNovaData.getValor().setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestNovaData.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/{dataInicio}/{dataFim}"), dataInicio, movimentoRequestTestDTO.getData())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andReturn());

        final String dataInicioNova = dataInicio.plusDays(1).format(DateTimeFormatter.ISO_DATE);
        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/{dataInicio}/{dataFim}"), dataInicioNova, requestNovaData.getData())
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void consultaEstoqueNaoExiste() {
        final String codigo = RandomStringUtils.random(10, true, true);
        super.iniciarAtivoValor(codigo, TipoAtivo.RV, this.getDataDiaUtil());

        final String dataInicio = super.getDataDiaUtil().minusDays(7).format(DateTimeFormatter.ISO_DATE);
        final String dataFim = super.getDataDiaUtil().minusDays(6).format(DateTimeFormatter.ISO_DATE);

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/{dataInicio}/{dataFim}"), dataInicio, dataFim)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_ADMIN"})
    void consultaPerfilAdmin() {
        final String dataInicio = super.getDataDiaUtil().minusDays(7).format(DateTimeFormatter.ISO_DATE);
        final String dataFim = super.getDataDiaUtil().minusDays(6).format(DateTimeFormatter.ISO_DATE);

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/{dataInicio}/{dataFim}"), dataInicio, dataFim)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn());
    }

    @Test
    void consultaNaoAutenticado() {
        final String dataInicio = super.getDataDiaUtil().minusDays(7).format(DateTimeFormatter.ISO_DATE);
        final String dataFim = super.getDataDiaUtil().minusDays(6).format(DateTimeFormatter.ISO_DATE);

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/{dataInicio}/{dataFim}"), dataInicio, dataFim)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void consultaPorData() {
        final String codigo = RandomStringUtils.random(10, true, true);
        final AtivoValor ativoValor = super.iniciarAtivoValor(codigo, TipoAtivo.RF, this.getDataDiaUtil());

        final LocalDate dataPosicao = super.getDataDiaUtil();
        final MovimentoRequestTestDTO movimentoVendaRV = super.getMovimentoRequestTestDTOMock(codigo, dataPosicao,
                QUANTIDADE_MAX_DEFAULT, ativoValor.getValor().setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoVendaRV.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final MovimentoRequestTestDTO movimentoCompraRV = super.getMovimentoRequestTestDTOMock(codigo, dataPosicao,
                1000, ativoValor.getValor().setScale(2, RoundingMode.DOWN));
        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoCompraRV.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final String codigoFundo = RandomStringUtils.random(10, true, true);
        final AtivoValor ativoValorFundo = super.iniciarAtivoValor(codigoFundo, TipoAtivo.FUNDO, this.getDataDiaUtil());

        final MovimentoRequestTestDTO movimentoVendaFundo = super.getMovimentoRequestTestDTOMock(codigoFundo, dataPosicao,
                QUANTIDADE_MAX_DEFAULT, ativoValorFundo.getValor().setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoVendaFundo.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final MovimentoRequestTestDTO movimentoCompraFundo = super.getMovimentoRequestTestDTOMock(codigoFundo, dataPosicao,
                100, ativoValorFundo.getValor().setScale(2, RoundingMode.DOWN));
        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoCompraFundo.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final LocalDate novaData = dataPosicao.plusDays(3);
        final AtivoValor ativoValorFundoNovaData = super.iniciarAtivoValor(codigoFundo, TipoAtivo.FUNDO, novaData);
        final MovimentoRequestTestDTO movimentoVendaFundoOutraData = super.getMovimentoRequestTestDTOMock(codigoFundo, novaData,
                QUANTIDADE_MAX_DEFAULT, ativoValorFundoNovaData.getValor().setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoVendaFundoOutraData.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final MovimentoRequestTestDTO movimentoCompraFundoOutraData = super.getMovimentoRequestTestDTOMock(codigoFundo, novaData,
                100, ativoValorFundoNovaData.getValor().setScale(2, RoundingMode.DOWN));
        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoCompraFundoOutraData.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final String codigoRF = RandomStringUtils.random(10, true, true);
        final AtivoValor ativoValorRF = super.iniciarAtivoValor(codigoRF, TipoAtivo.RF, this.getDataDiaUtil());
        final MovimentoRequestTestDTO movimentoRF = super.getMovimentoRequestTestDTOMock(codigoRF, dataPosicao,
                QUANTIDADE_MAX_DEFAULT, ativoValorRF.getValor().setScale(2, RoundingMode.DOWN));

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_VENDA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoRF.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final MovimentoRequestTestDTO movimentoCompraRF = super.getMovimentoRequestTestDTOMock(codigoRF, dataPosicao,
                100, ativoValorRF.getValor().setScale(2, RoundingMode.DOWN));
        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_COMPRA))
                .contentType(MediaType.APPLICATION_JSON)
                .content(movimentoCompraRF.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/posicao/{dataPosicao}"), dataPosicao.format(DateTimeFormatter.ISO_DATE))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalElements").value(3))
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void consultaPorDataEstoqueNaoExiste() {
        final String localDate = super.getDataDiaUtil().minusDays(1L).format(DateTimeFormatter.ISO_DATE);

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/posicao/{dataPosicao}"), localDate)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNoContent())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_ADMIN"})
    void consultaPorDataPerfilAdmin() {
        final String localDate = super.getDataDiaUtil().minusDays(1L).format(DateTimeFormatter.ISO_DATE);

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/posicao/{dataPosicao}"), localDate)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isForbidden())
                .andReturn());
    }

    @Test
    void consultaPorDataNaoAutenticado() {
        final String localDate = super.getDataDiaUtil().minusDays(1L).format(DateTimeFormatter.ISO_DATE);

        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/posicao/{dataPosicao}"), localDate)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized())
                .andReturn());
    }
}
