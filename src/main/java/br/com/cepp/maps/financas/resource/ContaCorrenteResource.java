package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.service.ContaCorrenteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

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

    @Autowired
    public ContaCorrenteResource(ContaCorrenteService contaCorrenteService) {
        this.contaCorrenteService = contaCorrenteService;
    }

    @PostMapping(path = "/credito", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE, "*/*;charset=UTF-8"})
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
            @ApiResponse(code = 403, message = "Acesso proibido ao usuário"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    public ResponseEntity<String> credito(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final LancamentoRequestDTO lancamentoRequestDTO,
                                          @RequestHeader(name = HEADER_CODIGO_USUARIO) @NotEmpty(message = "Header 'codigoUsuario' é obrigatório") String codigoUsuario) {
        this.contaCorrenteService.incluirCredito(lancamentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @PostMapping(path = "/debito", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE, "*/*;charset=UTF-8"})
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
            @ApiResponse(code = 403, message = "Acesso proibido ao usuário"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    public ResponseEntity<String> debito(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final LancamentoRequestDTO lancamentoRequestDTO,
                                         @RequestHeader(name = HEADER_CODIGO_USUARIO) @NotEmpty(message = "Header 'codigoUsuario' é obrigatório") String codigoUsuario) {
        this.contaCorrenteService.incluirDebito(lancamentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @GetMapping(path="/{codigoUsuario}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE, "*/*;charset=UTF-8"})
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
            @ApiResponse(code = 403, message = "Acesso proibido ao usuário"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    public ResponseEntity<String> consulta(@Valid @NotEmpty(message = "Objeto do request não encontrado") @PathVariable(name = HEADER_CODIGO_USUARIO) final String codigoUsuario) {
        ContaCorrente contaCorrente = this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(codigoUsuario);
        return ResponseEntity.ok(contaCorrente.getSaldoConta().toString());
    }
}
