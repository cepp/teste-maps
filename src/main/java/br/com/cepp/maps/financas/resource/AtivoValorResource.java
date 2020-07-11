package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.resource.dto.AtivoValorRequestDTO;
import br.com.cepp.maps.financas.service.AtivoValorService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@Api(value = "Valor Ativo por Data", tags = {"Ativos", "Posição"})
@Validated
@RestController
@RequestMapping("/valorativo")
public class AtivoValorResource {
    private final AtivoValorService service;

    @Autowired
    public AtivoValorResource(AtivoValorService service) {
        this.service = service;
    }

    @PostMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Incluir Valor Ativo", authorizations = {@Authorization(value = AUTHORIZATION)})
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
    public ResponseEntity<String> incluir(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final AtivoValorRequestDTO ativoValorRequestDTO) {
        this.service.incluir(ativoValorRequestDTO);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @DeleteMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Remover Valor Ativo", authorizations = {@Authorization(value = AUTHORIZATION)})
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
    public ResponseEntity<String> remover(@NotEmpty(message = "Campo 'codigoAtivo' é obrigatorio") @RequestParam(name = "codigoAtivo") final String codigoAtivo,
                                          @NotNull(message = "Campo 'data' é obrigatorio") @RequestParam(name = "data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate data) {
        this.service.remover(codigoAtivo, data);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }
}
