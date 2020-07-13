package br.com.cepp.maps.financas.service;

import br.com.cepp.maps.financas.model.Ativo;
import br.com.cepp.maps.financas.model.AtivoUsuario;
import br.com.cepp.maps.financas.model.dominio.TipoMovimento;
import br.com.cepp.maps.financas.repository.AtivoUsuarioRepository;
import br.com.cepp.maps.financas.resource.dto.MovimentoRequestDTO;
import br.com.cepp.maps.financas.resource.handler.SaldoInsuficienteException;
import br.com.cepp.maps.financas.utils.BigDecimalUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Log4j2
@Service
@Validated
public class AtivoUsuarioService {
    private final AtivoUsuarioRepository repository;

    public AtivoUsuarioService(AtivoUsuarioRepository repository) {
        this.repository = repository;
    }

    public AtivoUsuario atualizar(@Valid @NotNull(message = "Movimento é obrigatório") final MovimentoRequestDTO movimento,
                                  @NotEmpty(message = "Campo 'codigoUsuario' é obrigatório") final String codigoUsuario,
                                  @Valid @NotNull(message = "Movimento é obrigatório") final Ativo ativo,
                                  @NotNull(message = "Campo 'tipoMovimento' é obrigatório") final TipoMovimento tipoMovimento) {
        Optional<AtivoUsuario> optionalAtivo = this.repository.findByAtivo_CodigoAndDataPosicaoAndCodigoUsuario(movimento.getAtivo(),
                movimento.getData(), codigoUsuario);

        final AtivoUsuario ativoUsuario = optionalAtivo.orElse(new AtivoUsuario(null, BigDecimal.ZERO.setScale(2, RoundingMode.DOWN),
                ativo, movimento.getData(), BigDecimal.ZERO.setScale(2, RoundingMode.DOWN),
                codigoUsuario));

        final BigDecimal quantidade = BigDecimalUtils.ajustarValor(movimento.getQuantidade(), ativoUsuario.getQuantidade(), tipoMovimento);
        final BigDecimal valor = BigDecimalUtils.ajustarValor(movimento.getValor(), ativoUsuario.getValor(), tipoMovimento);

        if(BigDecimal.ZERO.compareTo(quantidade) > 0 || BigDecimal.ZERO.compareTo(valor) > 0) {
            throw new SaldoInsuficienteException();
        }

        final AtivoUsuario estoqueAtualizado = ativoUsuario.comQuantidadeEValor(quantidade, valor);

        return this.repository.save(estoqueAtualizado);
    }
}
