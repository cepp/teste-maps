package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.resource.dto.LancamentoRequestTestDTO;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static br.com.cepp.maps.financas.config.MockMvcConfig.UTF_8;
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
    @WithMockUser(authorities={"ROLE_USER"})
    void credito() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = super.getLancamentoRequestRestMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
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
    @WithMockUser(authorities={"ROLE_USER"})
    void creditoValidarData() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = super.getLancamentoRequestRestMock();
        lancamentoRequestTestDTO.setData(null);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'data' é obrigatório]"))
                .andReturn());

        lancamentoRequestTestDTO.setData(Strings.EMPTY);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());

        lancamentoRequestTestDTO.setData("3213241");

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());


        lancamentoRequestTestDTO.setData("99999-88-99");

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void creditoValidarValor() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = super.getLancamentoRequestRestMock();
        lancamentoRequestTestDTO.setValor(null);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'valor' é obrigatório]"))
                .andReturn());

        lancamentoRequestTestDTO.setData(Strings.EMPTY);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());

        lancamentoRequestTestDTO.setData("sdadfad");

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());


        lancamentoRequestTestDTO.setData("99999-88-99");

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void creditoValidarDescricao() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = super.getLancamentoRequestRestMock();
        lancamentoRequestTestDTO.setDescricao(null);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'descricao' é obrigatório]"))
                .andReturn());

        lancamentoRequestTestDTO.setDescricao(Strings.EMPTY);

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void debito() {
        final LancamentoRequestTestDTO lancamentoRequestCredito = super.getLancamentoRequestRestMock();

        assertDoesNotThrow(() -> super.getMockMvc().perform(post(URI_V1.concat(END_POINT_CREDITO))
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
    @WithMockUser(authorities={"ROLE_USER"})
    void consulta() {
        final String data = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/saldo"))
                .queryParam("data", data)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().encoding(UTF_8))
                .andExpect(status().isOk())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void consultaNotFound() {
        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().encoding(UTF_8))
                .andExpect(status().isNotFound())
                .andReturn());
    }

    @Test
    @WithMockUser(authorities={"ROLE_USER"})
    void consultaNoContent() {
        final String data = LocalDate.now().plusMonths(4).format(DateTimeFormatter.ISO_DATE);
        assertDoesNotThrow(() -> super.getMockMvc().perform(get(URI_V1.concat("/saldo"))
                .queryParam("data", data)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().encoding(UTF_8))
                .andExpect(status().isNotFound())
                .andReturn());
    }
}
