package br.com.cepp.maps.financas.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Immutable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"movimentos"})
@ToString(exclude = {"movimentos"})
@Entity
@Immutable
public class AtivoValor implements Serializable {
    private static final long serialVersionUID = 2001890995695664392L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @NotNull(message = "Campo 'data' é obrigatório")
    private final LocalDate data;
    @Digits(integer = 8, fraction = 8, message = "Campo 'valor' inválido")
    @NotNull(message = "Campo 'valor' é obrigatório")
    private final BigDecimal valor;
    @NotNull(message = "Campo 'ativo' é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    private final Ativo ativo;
    @OneToMany(mappedBy = "ativoValor", fetch = FetchType.LAZY)
    private final List<Movimento> movimentos;

    private AtivoValor() {
        this.id = null;
        this.data = null;
        this.valor = null;
        this.ativo = null;
        this.movimentos = null;
    }
}
