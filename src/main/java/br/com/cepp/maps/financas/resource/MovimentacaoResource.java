package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.Estoque;
import br.com.cepp.maps.financas.resource.dto.EstoqueResponseDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestDTO;
import br.com.cepp.maps.financas.service.MovimentoService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.HEADER_CODIGO_USUARIO;
import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Log4j2
@Api(value = "Movimentação", tags = {"Movimentação"})
@Validated
@RestController
@RequestMapping("/movimentacao")
public class MovimentacaoResource {
    private final MovimentoService service;

    @Autowired
    public MovimentacaoResource(MovimentoService service) {
        this.service = service;
    }

    @PostMapping(path = "/compra", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Compra de ativos", authorizations = {@Authorization(value = AUTHORIZATION)})
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
    public ResponseEntity<String> compra(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final MovimentoRequestDTO movimentoRequestDTO,
                                         @RequestHeader(name = HEADER_CODIGO_USUARIO) @NotEmpty(message = "Header 'codigoUsuario' é obrigatório") String codigoUsuario) {
        this.service.compra(movimentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @PostMapping(path = "/venda", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Venda de ativos", authorizations = {@Authorization(value = AUTHORIZATION)})
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
    public ResponseEntity<String> venda(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final MovimentoRequestDTO movimentoRequestDTO,
                                        @RequestHeader(name = HEADER_CODIGO_USUARIO) @NotEmpty(message = "Header 'codigoUsuario' é obrigatório") String codigoUsuario) {
        this.service.venda(movimentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @GetMapping(path = "/ativo/{ativo}/{dataPosicao}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consulta saldo de ativos", authorizations = {@Authorization(value = AUTHORIZATION)})
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
    public ResponseEntity<EstoqueResponseDTO> consulta(@NotEmpty(message = "Campo 'ativo' é obrigatório") @PathVariable(name = "ativo") final String ativo,
                                                       @NotNull(message = "Campo 'dataPosicao' é obrigatório") @PathVariable(name = "dataPosicao") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataPosicao) {
        Estoque estoque = this.service.buscarPosicaoPorCodigoEData(ativo, dataPosicao);
        EstoqueResponseDTO estoqueResponseDTO = this.converterEntidadeParaDTO(estoque);
        return ResponseEntity.ok(estoqueResponseDTO);
    }

    @GetMapping(path = "/{dataPosicao}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consulta saldo de ativos", authorizations = {@Authorization(value = AUTHORIZATION)})
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
    public ResponseEntity<List<EstoqueResponseDTO>> consultaPorData(@NotNull(message = "Campo 'dataPosicao' é obrigatório") @PathVariable(name = "dataPosicao") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataPosicao) {
        return ResponseEntity.ok(this.service.buscarPorDataPosicao(dataPosicao));
    }

    private EstoqueResponseDTO converterEntidadeParaDTO(Estoque estoque) {
        return new EstoqueResponseDTO(estoque.getAtivo().getCodigo(), estoque.getAtivo().getTipoAtivo(), estoque.getQuantidade(), estoque.getDataPosicao(), null, null, null);
    }
}
