package br.com.cepp.maps.financas.resource;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.resource.dto.AtivoRequestDTO;
import br.com.cepp.maps.financas.service.AtivoService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import javax.validation.constraints.Pattern;

import static br.com.cepp.maps.financas.resource.ContaCorrenteResource.MSG_OPERACAO_REALIZADA_COM_SUCESSO;

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

    @PostMapping(produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Cadastro de Ativo", description = "Cadastro de Ativo")
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
            @ApiResponse(responseCode = "409", description = "Registro já existe", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) }),
            @ApiResponse(responseCode = "500", description = "Erro interno", content = { @Content(mediaType = MimeTypeUtils.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = String.class)) })
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> incluir(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final AtivoRequestDTO ativoRequestDTO) {
        this.service.incluir(ativoRequestDTO);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @PutMapping(path = "/{codigo}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Alteração de Ativo", description = "Alteração de Ativo")
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
    public ResponseEntity<String> alterar(@Valid @NotNull(message = "Objeto do request não encontrado") @RequestBody final AtivoRequestDTO ativoRequestDTO,
                                          @PathVariable(name = "codigo") @NotEmpty(message = "Campo 'codigo' é obrigatorio") @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Campo 'codigo' inválido") String codigo) {
        this.service.alterar(ativoRequestDTO, codigo);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @DeleteMapping(path = "/{codigo}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Remover Ativo", description = "Remover de Ativo")
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
    public ResponseEntity<String> remover(@PathVariable(name = "codigo") @NotEmpty(message = "Campo 'codigo' é obrigatorio") String codigo) {
        this.service.remover(codigo);
        return ResponseEntity.ok(MSG_OPERACAO_REALIZADA_COM_SUCESSO);
    }

    @GetMapping(path="/{codigo}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @Operation(summary = "Consulta posição do ativo por código", description = "Consulta Posição Ativo")
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
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<AtivoRequestDTO> consultaPorCodigo(@Valid @NotEmpty(message = "Objeto do request não encontrado") @PathVariable(name = "codigo") final String codigo) {
        final Ativo ativo = this.service.buscarPorCodigo(codigo);
        final AtivoRequestDTO requestDTO = new AtivoRequestDTO(ativo.getCodigo(), ativo.getNome(), ativo.getTipoAtivo(),
                ativo.getDataEmissao(), ativo.getDataVencimento());
        return ResponseEntity.ok(requestDTO);
    }
}
