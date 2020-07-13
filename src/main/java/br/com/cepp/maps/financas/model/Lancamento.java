package br.com.cepp.maps.financas.model;

import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
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
public class Lancamento implements Serializable {
    private static final long serialVersionUID = -7782002581779012788L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Digits(integer = 15, fraction = 2, message = "Campo 'valor' inválido")
    @DecimalMin(value = "0.01", message = "Campo 'valor' inválido")
    @NotNull(message = "Campo 'valor' é obrigatório")
    private final BigDecimal valor;
    @NotNull(message = "Campo 'dataMovimento' é obrigatório")
    private final LocalDate dataMovimento;
    @NotEmpty(message = "Campo 'descricao' é obrigatório")
    private final String descricao;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Campo 'natureza'é obrigatório")
    private final TipoNatureza natureza;
    @NotNull(message = "Campo 'contaCorrente' é obrigatório")
    private final Long codigoContaCorrente;

    private Lancamento() {
        this.id = null;
        this.valor = null;
        this.dataMovimento = null;
        this.descricao = null;
        this.natureza = null;
        this.codigoContaCorrente = null;
    }
}
