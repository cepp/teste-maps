package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.resource.dto.AtivoValorRequestDTO;
import br.com.cepp.maps.financas.service.AtivoValorService;
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
import org.springframework.util.MimeTypeUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO;

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
    @Operation(summary = "Cadastrar Posição Ativo", description = "Cadastrar Posição Ativo")
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
            @ApiResponse(responseCode = "409", description = "Recurso já existe", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) })
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> incluir(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final AtivoValorRequestDTO ativoValorRequestDTO) {
        this.service.incluir(ativoValorRequestDTO);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @DeleteMapping(path = "/{codigoAtivo}/{data}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remover Posição Ativo", description = "Remover Posição Ativo")
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> remover(@NotEmpty(message = "Campo 'codigoAtivo' é obrigatorio") @PathVariable(name = "codigoAtivo") final String codigoAtivo,
                                          @NotNull(message = "Campo 'data' é obrigatorio") @PathVariable(name = "data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate data) {
        this.service.remover(codigoAtivo, data);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }
}
