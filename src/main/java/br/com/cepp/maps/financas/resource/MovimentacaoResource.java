package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.resource.dto.EstoqueResponseDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.MovimentoResponseDTO;
import br.com.cepp.maps.financas.service.MovimentoService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Compra de Ativo", description = "Compra de Ativo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MSG_OPERACAO_REALIZADA_COM_SUCESSO, content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "403", description = "Acesso proibido ao usuário", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) })
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> compra(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final MovimentoRequestDTO movimentoRequestDTO) {
        final String codigoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        this.service.compra(movimentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @PostMapping(path = "/venda", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Venda de Ativo", description = "Venda de Ativo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MSG_OPERACAO_REALIZADA_COM_SUCESSO, content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "403", description = "Acesso proibido ao usuário", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) })
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<String> venda(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final MovimentoRequestDTO movimentoRequestDTO) {
        final String codigoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        this.service.venda(movimentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @GetMapping(path = "/{dataInicio}/{dataFim}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Consulta Movimentações de Ativo por Período", description = "Consulta Movimentações de Ativo por Período")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MSG_OPERACAO_REALIZADA_COM_SUCESSO, content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "403", description = "Acesso proibido ao usuário", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) })
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<MovimentoResponseDTO>> consultaMovimentacoesPorPeriodo(@NotNull(message = "Campo 'dataFim' é obrigatório") @PathVariable(name = "dataInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataInicio,
                                                                                      @NotNull(message = "Campo 'dataFim' é obrigatório") @PathVariable(name = "dataFim") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataFim,
                                                                                      Pageable pageable) {
        final String codigoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(this.service.buscarPosicaoPorPeriodo(dataInicio, dataFim, pageable, codigoUsuario));
    }

    @GetMapping(path = "/posicao/{dataPosicao}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Consulta Saldo de Ativo por Data", description = "Consulta Saldo de Ativo por Data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = MSG_OPERACAO_REALIZADA_COM_SUCESSO, content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "400", description = "Erro de validação", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "403", description = "Acesso proibido ao usuário", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) })
    })
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<Page<EstoqueResponseDTO>> consultaPosicaoPorData(@NotNull(message = "Campo 'dataPosicao' é obrigatório") @PathVariable(name = "dataPosicao") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataPosicao,
                                                                           Pageable pageable) {
        return ResponseEntity.ok(this.service.buscarPorDataPosicao(dataPosicao, pageable));
    }
}
