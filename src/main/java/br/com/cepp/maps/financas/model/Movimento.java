package br.com.cepp.maps.financas.model;

import br.com.cepp.maps.financas.model.dominio.TipoMovimento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Immutable
public class Movimento implements Serializable {
    private static final long serialVersionUID = -2348473212066764289L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long codigo;
    @NotNull(message = "Campo 'ativo' é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    private final AtivoValor ativoValor;
    @NotNull(message = "Campo 'dataMovimento' é obrigatório")
    private final LocalDate dataMovimento;
    @DecimalMin(value = "0.01", message = "Campo 'quantidade' inválido")
    @Digits(integer = 8, fraction = 2, message = "Campo 'quantidade' inválido")
    @NotNull(message = "Campo 'quantidade' é obrigatório")
    private final BigDecimal quantidade;
    @DecimalMin(value = "0.01", message = "Campo 'valor' inválido")
    @Digits(integer = 15, fraction = 2, message = "Campo 'valor' inválido")
    @NotNull(message = "Campo 'valor' é obrigatório")
    private final BigDecimal valor;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Campo 'tipoMovimento'é obrigatório")
    private final TipoMovimento tipoMovimento;
    @NotNull(message = "Campo 'ativoUsuario' é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    private final AtivoUsuario ativoUsuario;

    private Movimento() {
        this.codigo = null;
        this.ativoValor = null;
        this.dataMovimento = null;
        this.quantidade = null;
        this.valor = null;
        this.tipoMovimento = null;
        this.ativoUsuario = null;
    }

    public Movimento comCodigoAtivoUsuario(final AtivoUsuario stivoUsuario) {
        return new Movimento(this.codigo, this.ativoValor, this.dataMovimento, this.quantidade, this.valor,
                this.tipoMovimento, stivoUsuario);
    }
}
