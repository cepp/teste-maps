package br.com.cepp.maps.financas.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Immutable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
public class Estoque implements Serializable {
    private static final long serialVersionUID = -6898611806659538972L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;
    @DecimalMin(value = "0.00", message = "Campo 'quantidade' inválido")
    @Digits(integer = 8, fraction = 2, message = "Campo 'quantidade' inválido")
    @NotNull(message = "Campo 'quantidade' é obrigatório")
    private final BigDecimal quantidade;
    @NotNull(message = "Campo 'ativo' é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private final Ativo ativo;
    @NotNull(message = "Campo 'dataPosicao' é obrigatório")
    private final LocalDate dataPosicao;
    @DecimalMin(value = "0.00", message = "Campo 'valor' inválido")
    @Digits(integer = 15, fraction = 2, message = "Campo 'valor' inválido")
    @NotNull(message = "Campo 'valor' é obrigatório")
    private final BigDecimal valor;

    private Estoque() {
        this.id = null;
        this.quantidade = null;
        this.ativo = null;
        this.dataPosicao = null;
        this.valor = null;
    }

    public Estoque comQuantidadeEValor(@DecimalMin(value = "0.01", message = "Campo 'quantidade' inválido")
                                       @Digits(integer = 8, fraction = 2, message = "Campo 'quantidade' inválido")
                                       @NotNull(message = "Campo 'quantidade' é obrigatório") BigDecimal quantidade,
                                       @Digits(integer = 15, fraction = 2, message = "Campo 'valor' inválido")
                                       @NotNull(message = "Campo 'valor' é obrigatório") BigDecimal valor) {
        return new Estoque(this.id, quantidade, this.ativo, this.dataPosicao, valor);
    }
}
