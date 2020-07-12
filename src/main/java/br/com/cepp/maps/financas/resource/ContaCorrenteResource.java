package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.SaldoResponseDTO;
import br.com.cepp.maps.financas.service.ContaCorrenteService;
import br.com.cepp.maps.financas.service.LancamentoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static br.com.cepp.maps.financas.config.AplicacaoConfig.CODIGO_USUARIO_GLOBAL;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@Api(value = "Conta Corrente", tags = {"Conta Corrente"})
@Validated
@RestController
@RequestMapping("/contacorrente")
public class ContaCorrenteResource {

    public static final String MSG_OPERACAO_REALIZADA_COM_SUCESSO = "Operação realizada com sucesso";
    public static final String HEADER_CODIGO_USUARIO = "codigoUsuario";
    private final ContaCorrenteService contaCorrenteService;
    private final LancamentoService lancamentoService;

    @Autowired
    public ContaCorrenteResource(ContaCorrenteService contaCorrenteService, LancamentoService lancamentoService) {
        this.contaCorrenteService = contaCorrenteService;
        this.lancamentoService = lancamentoService;
    }

    @PostMapping(path = "/credito", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Lançamento de Crédito", authorizations = {@Authorization(value = AUTHORIZATION)})
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = "Token autorização", required = true,
                    paramType = "header", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = MSG_OPERACAO_REALIZADA_COM_SUCESSO),
            @ApiResponse(code = 204, message = "Registro não encontrado"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    public ResponseEntity<String> credito(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final LancamentoRequestDTO lancamentoRequestDTO,
                                          @RequestHeader(name = HEADER_CODIGO_USUARIO) @NotEmpty(message = "Header 'codigoUsuario' é obrigatório") String codigoUsuario) {
        this.lancamentoService.incluirCredito(lancamentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @PostMapping(path = "/debito", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Lançamento de Débito", authorizations = {@Authorization(value = AUTHORIZATION)})
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = "Token autorização", required = true,
                    paramType = "header", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = MSG_OPERACAO_REALIZADA_COM_SUCESSO),
            @ApiResponse(code = 204, message = "Registro não encontrado"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    public ResponseEntity<String> debito(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final LancamentoRequestDTO lancamentoRequestDTO,
                                         @RequestHeader(name = HEADER_CODIGO_USUARIO) @NotEmpty(message = "Header 'codigoUsuario' é obrigatório") String codigoUsuario) {
        this.lancamentoService.incluirDebito(lancamentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @GetMapping(path="/saldo", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consulta Saldo da Conta", authorizations = {@Authorization(value = AUTHORIZATION)})
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = "Token autorização", required = true,
                    paramType = "header", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = MSG_OPERACAO_REALIZADA_COM_SUCESSO),
            @ApiResponse(code = 204, message = "Registro não encontrado"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    public ResponseEntity<SaldoResponseDTO> consulta(@NotNull(message = "Campo 'data' é obrigatorio") @RequestParam(name = "data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate data) {
        ContaCorrente contaCorrente = this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(CODIGO_USUARIO_GLOBAL, data);
        final SaldoResponseDTO saldoResponseDTO = new SaldoResponseDTO(contaCorrente.getSaldoConta());
        return ResponseEntity.ok(saldoResponseDTO);
    }
}
