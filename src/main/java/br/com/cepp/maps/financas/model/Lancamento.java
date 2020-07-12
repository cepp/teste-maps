package br.com.cepp.maps.financas.model;

import br.com.cepp.maps.financas.model.dominio.TipoNatureza;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
@Entity
@Immutable
public class Lancamento implements Serializable {
    private static final long serialVersionUID = -7782002581779012788L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Digits(integer = 15, fraction = 2, message = "Campo 'valor' inválido")
    @DecimalMin(value = "0.01", message = "Campo 'valor' inválido")
    @NotNull(message = "Campo 'valor' é obrigatório")
    private BigDecimal valor;
    @NotNull(message = "Campo 'dataMovimento' é obrigatório")
    private LocalDate dataMovimento;
    @NotEmpty(message = "Campo 'descricao' é obrigatório")
    private String descricao;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Campo 'natureza'é obrigatório")
    private TipoNatureza natureza;
    @NotNull(message = "Campo 'contaCorrente' é obrigatório")
    private Long codigoContaCorrente;

}
