package br.com.cepp.maps.financas.resource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@Api(value = "Movimentação", tags = {"Movimentação"})
@Validated
@RestController
@RequestMapping("/movimentacao")
public class MovimentacaoResource {
    @PostMapping(path = "/compra", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE, "*/*;charset=UTF-8"})
    @ApiOperation(value = "Compra de ativos", authorizations = {@Authorization(value = AUTHORIZATION)})
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = "Token autorização", required = true,
                    paramType = "header", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = MSG_OPERACAO_REALIZADA_COM_SUCESSO),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 403, message = "Acesso proibido ao usuário"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    public ResponseEntity<String> compra() {
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @PostMapping(path = "/venda", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE, "*/*;charset=UTF-8"})
    @ApiOperation(value = "Venda de ativos", authorizations = {@Authorization(value = AUTHORIZATION)})
    @ApiImplicitParams({
            @ApiImplicitParam(name = AUTHORIZATION, value = "Token autorização", required = true,
                    paramType = "header", dataTypeClass = String.class)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = MSG_OPERACAO_REALIZADA_COM_SUCESSO),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 403, message = "Acesso proibido ao usuário"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    public ResponseEntity<String> venda() {
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }
}
