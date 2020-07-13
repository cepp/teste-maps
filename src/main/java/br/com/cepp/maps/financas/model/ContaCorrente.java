package br.com.cepp.maps.financas.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Immutable
public class ContaCorrente implements Serializable {
    private static final long serialVersionUID = 719025958082569569L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @Digits(integer = 15, fraction = 2, message = "Campo 'saldoConta' inválido")
    @Min(value = 0, message = "Campo 'saldoConta' inválido")
    @NotNull(message = "Campo 'saldoConta' é obrigatório")
    private final BigDecimal saldoConta;
    @NotEmpty(message = "Campo 'usuario' é obrigatório")
    private final String codigoUsuario;
    @NotNull(message = "Campo 'data' é obrigatório")
    private final LocalDate data;

    private ContaCorrente() {
        this.id = null;
        this.saldoConta = null;
        this.codigoUsuario = null;
        this.data = null;
    }

    public ContaCorrente comSaldoAtualizado(BigDecimal saldoAtualizado) {
        return new ContaCorrente(this.id, saldoAtualizado, this.codigoUsuario, this.data);
    }
}
