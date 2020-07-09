package br.com.cepp.maps.financas.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
@Entity
@Immutable
public class ContaCorrente implements Serializable {
    private static final long serialVersionUID = 719025958082569569L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Digits(integer = 15, fraction = 0, message = "Campo 'saldoConta' inválido")
    @Min(value = 0, message = "Campo 'saldoConta' inválido")
    @NotNull(message = "Campo 'saldoConta' é obrigatório")
    private BigDecimal saldoConta;
    @NotEmpty(message = "Campo 'usuario' é obrigatório")
    private String codigoUsuario;

    public ContaCorrente comSaldoAtualizado(BigDecimal saldoAtualizado) {
        return new ContaCorrente(this.id, saldoAtualizado, this.codigoUsuario);
    }
}
