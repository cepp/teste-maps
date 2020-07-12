package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.resource.dto.LancamentoRequestTestDTO;
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

import static br.com.cepp.maps.financas.config.AplicacaoConfig.CODIGO_USUARIO_GLOBAL;
import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.HEADER_CODIGO_USUARIO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ContaCorrenteResourceTest extends AbstractResourceTest {
    private static final String URI_V1 = "/contacorrente/";
    public static final String END_POINT_CREDITO = "credito";

    @Test
    void credito() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = super.getLancamentoRequestRestMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{'blajfdkj': 8765, 'jkefhj': 'sdjhef'}")
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    void creditoValidarData() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = super.getLancamentoRequestRestMock();
        lancamentoRequestTestDTO.setData(null);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'data' é obrigatório]"))
                .andReturn());

        lancamentoRequestTestDTO.setData(Strings.EMPTY);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());

        lancamentoRequestTestDTO.setData("3213241");

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());


        lancamentoRequestTestDTO.setData("99999-88-99");

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    void creditoValidarValor() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = super.getLancamentoRequestRestMock();
        lancamentoRequestTestDTO.setValor(null);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'valor' é obrigatório]"))
                .andReturn());

        lancamentoRequestTestDTO.setData(Strings.EMPTY);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());

        lancamentoRequestTestDTO.setData("sdadfad");

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());


        lancamentoRequestTestDTO.setData("99999-88-99");

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    void creditoValidarDescricao() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = super.getLancamentoRequestRestMock();
        lancamentoRequestTestDTO.setDescricao(null);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'descricao' é obrigatório]"))
                .andReturn());

        lancamentoRequestTestDTO.setDescricao(Strings.EMPTY);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    void debito() {
        final LancamentoRequestTestDTO lancamentoRequestCredito = super.getLancamentoRequestRestMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestCredito.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        final LancamentoRequestTestDTO lancamentoRequestDebito = super.getLancamentoRequestRestMock();
        lancamentoRequestDebito.setValor(lancamentoRequestCredito.getValor());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat("debito"))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestDebito.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().encoding(UTF_8))
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    @Test
    void consulta() {
        final String data = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/saldo"))
                .queryParam("data", data)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().encoding(UTF_8))
                .andExpect(status().isOk())
                .andReturn());
    }

    @Test
    void consultaNotFound() {
        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().encoding(UTF_8))
                .andExpect(status().isNotFound())
                .andReturn());
    }

    @Test
    void consultaNoContent() {
        final String data = LocalDate.now().minusDays(4).format(DateTimeFormatter.ISO_DATE);
        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/saldo"))
                .queryParam("data", data)
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .header(HEADER_CODIGO_USUARIO, CODIGO_USUARIO_GLOBAL)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().encoding(UTF_8))
                .andExpect(status().isNoContent())
                .andReturn());
    }
}
