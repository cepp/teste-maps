package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.resource.dto.EstoqueResponseDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoResponseDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
    @ApiResponses({
            @ApiResponse(code = 200, message = MSG_OPERACAO_REALIZADA_COM_SUCESSO),
            @ApiResponse(code = 204, message = "Registro não encontrado"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> compra(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final MovimentoRequestDTO movimentoRequestDTO) {
        final String codigoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        this.service.compra(movimentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @PostMapping(path = "/venda", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Venda de ativos", authorizations = {@Authorization(value = AUTHORIZATION)})
    @ApiResponses({
            @ApiResponse(code = 200, message = MSG_OPERACAO_REALIZADA_COM_SUCESSO),
            @ApiResponse(code = 204, message = "Registro não encontrado"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> venda(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final MovimentoRequestDTO movimentoRequestDTO) {
        final String codigoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        this.service.venda(movimentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @GetMapping(path = "/{dataInicio}/{dataFim}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Consulta saldo de ativos", authorizations = {@Authorization(value = AUTHORIZATION)})
    @ApiResponses({
            @ApiResponse(code = 200, message = MSG_OPERACAO_REALIZADA_COM_SUCESSO),
            @ApiResponse(code = 204, message = "Registro não encontrado"),
            @ApiResponse(code = 400, message = "Erro de validação"),
            @ApiResponse(code = 401, message = "Não autorizado"),
            @ApiResponse(code = 404, message = "Não encontrado"),
            @ApiResponse(code = 500, message = "Erro interno")
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<MovimentoResponseDTO>> consultaMovimentacoesPorPeriodo(@NotNull(message = "Campo 'dataFim' é obrigatório") @PathVariable(name = "dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataInicio,
                                                                                      @NotNull(message = "Campo 'dataFim' é obrigatório") @PathVariable(name = "dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataFim,
                                                                                      Pageable pageable) {
        return ResponseEntity.ok(this.service.buscarPosicaoPorPeriodo(dataInicio, dataFim, pageable));
    }

    @GetMapping(path = "/posicao/{dataPosicao}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
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
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<EstoqueResponseDTO>> consultaPosicaoPorData(@NotNull(message = "Campo 'dataPosicao' é obrigatório") @PathVariable(name = "dataPosicao") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataPosicao,
                                                                           Pageable pageable) {
        return ResponseEntity.ok(this.service.buscarPorDataPosicao(dataPosicao, pageable));
    }
}
