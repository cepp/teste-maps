package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.service.AtivoService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@Api(value = "Ativos", tags = {"Ativos"})
@Validated
@RestController
@RequestMapping("/ativos")
public class AtivoResource {
    private final AtivoService service;

    @Autowired
    public AtivoResource(AtivoService service) {
        this.service = service;
    }

    @PostMapping(produces = {MimeTypeUtils.APPLICATION_JSON_VALUE, "*/*;charset=UTF-8"})
    @ApiOperation(value = "Incluir Ativo", authorizations = {@Authorization(value = AUTHORIZATION)})
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
            @ApiResponse(code = 409, message = "Recurso já existe"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    public ResponseEntity<String> incluir(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final AtivoRequestDTO ativoRequestDTO) {
        this.service.incluir(ativoRequestDTO);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @PutMapping(path = "/{codigo}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE, "*/*;charset=UTF-8"})
    @ApiOperation(value = "Alterar Ativo", authorizations = {@Authorization(value = AUTHORIZATION)})
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
    public ResponseEntity<String> alterar(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final AtivoRequestDTO ativoRequestDTO,
                                          @PathVariable(name = "codigo") @NotEmpty(message = "Campo 'codigo' é obrigatorio") String codigo) {
        this.service.alterar(ativoRequestDTO, codigo);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @DeleteMapping(path = "/{codigo}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE, "*/*;charset=UTF-8"})
    @ApiOperation(value = "Remover Ativo", authorizations = {@Authorization(value = AUTHORIZATION)})
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
    public ResponseEntity<String> remover(@PathVariable(name = "codigo") @NotEmpty(message = "Campo 'codigo' é obrigatorio") String codigo) {
        this.service.remover(codigo);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @GetMapping(path="/{codigo}", produces = {MimeTypeUtils.APPLICATION_JSON_VALUE, "*/*;charset=UTF-8"})
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
    public ResponseEntity<AtivoRequestDTO> consultaPorCodigo(@Valid @NotEmpty(message = "Objeto do request não encontrado") @PathVariable(name = "codigo") final String codigo) {
        final Ativo ativo = this.service.buscarPorCodigo(codigo);
        final AtivoRequestDTO requestDTO = new AtivoRequestDTO(ativo.getCodigo(), ativo.getNome(), ativo.getTipoAtivo());
        return ResponseEntity.ok(requestDTO);
    }
}
