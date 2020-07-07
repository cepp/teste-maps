package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.resource.dto.LancamentoRequestTestDTO;
import br.com.cepp.maps.financas.resource.serialization.FinancasLocalDateDeserializer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContaCorrenteResourceTest {
    private static final String URI_CHAVE_V1 = "/contacorrente/";
    public static final String UTF_8 = "UTF-8";
    public static final String END_POINT_CREDITO = "credito";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding(UTF_8);
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @Test
    void credito() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = this.getLancamentoRequestMock();

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    @Test
    void creditoValidarData() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = this.getLancamentoRequestMock();
        lancamentoRequestTestDTO.setData(null);

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'data' é obrigatório]"))
                .andReturn());

        lancamentoRequestTestDTO.setData(Strings.EMPTY);

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());

        lancamentoRequestTestDTO.setData("3213241");

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());


        lancamentoRequestTestDTO.setData("99999-88-99");

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    void creditoValidarValor() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = this.getLancamentoRequestMock();
        lancamentoRequestTestDTO.setValor(null);

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'valor' é obrigatório]"))
                .andReturn());

        lancamentoRequestTestDTO.setData(Strings.EMPTY);

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());

        lancamentoRequestTestDTO.setData("sdadfad");

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());


        lancamentoRequestTestDTO.setData("99999-88-99");

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    void creditoValidarDescricao() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = this.getLancamentoRequestMock();
        lancamentoRequestTestDTO.setDescricao(null);

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("[Campo 'descricao' é obrigatório]"))
                .andReturn());

        lancamentoRequestTestDTO.setDescricao(Strings.EMPTY);

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat(END_POINT_CREDITO))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andReturn());
    }

    @Test
    void debito() {
        final LancamentoRequestTestDTO lancamentoRequestTestDTO = this.getLancamentoRequestMock();

        assertDoesNotThrow(() -> this.mockMvc.perform(post(URI_CHAVE_V1.concat("debito"))
                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(lancamentoRequestTestDTO.toJson())
                .characterEncoding(UTF_8))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(content().encoding(UTF_8))
                .andExpect(status().isOk())
                .andExpect(content().string(ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO))
                .andReturn());
    }

    private LancamentoRequestTestDTO getLancamentoRequestMock() {
        LancamentoRequestTestDTO lancamento = new LancamentoRequestTestDTO();
        lancamento.setData(LocalDateTime.now().format(DateTimeFormatter.ofPattern(FinancasLocalDateDeserializer.DATE_FORMAT)));
        lancamento.setDescricao(RandomStringUtils.random(10, true, false));
        lancamento.setValor(RandomStringUtils.random(5, false, true));
        return lancamento;
    }
}
