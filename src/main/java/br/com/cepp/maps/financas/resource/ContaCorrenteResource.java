package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.ContaCorrente;
import br.com.cepp.maps.financas.resource.dto.LancamentoRequestDTO;
import br.com.cepp.maps.financas.resource.dto.SaldoResponseDTO;
import br.com.cepp.maps.financas.service.ContaCorrenteService;
import br.com.cepp.maps.financas.service.LancamentoService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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
    @Operation(summary = "Lançamento Crédito na Conta", description = "Lançamento Crédito na Conta")
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
    public ResponseEntity<String> credito(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final LancamentoRequestDTO lancamentoRequestDTO) {
        final String codigoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        this.lancamentoService.incluirCredito(lancamentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @PostMapping(path = "/debito", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lançamento Débito na Conta", description = "Lançamento Débito na Conta")
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
    public ResponseEntity<String> debito(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final LancamentoRequestDTO lancamentoRequestDTO) {
        final String codigoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        this.lancamentoService.incluirDebito(lancamentoRequestDTO, codigoUsuario);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @GetMapping(path="/saldo", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Consulta Saldo da Conta", description = "Consulta Saldo da Conta")
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
    public ResponseEntity<SaldoResponseDTO> consulta(@NotNull(message = "Campo 'data' é obrigatorio") @RequestParam(name = "data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate data) {
        final String codigoUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        ContaCorrente contaCorrente = this.contaCorrenteService.buscarContaCorrentePorCodigoUsuario(codigoUsuario, data);
        final SaldoResponseDTO saldoResponseDTO = new SaldoResponseDTO(contaCorrente.getSaldoConta());
        return ResponseEntity.ok(saldoResponseDTO);
    }
}
