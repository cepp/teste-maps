package br.com.cepp.maps.financas.model;

import br.com.cepp.maps.financas.model.dominio.TipoMovimento;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Immutable;

import javax.persistence.CascadeType;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
@Entity
@Immutable
public class Movimento implements Serializable {
    private static final long serialVersionUID = -2348473212066764289L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    @NotNull(message = "Campo 'ativo' é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AtivoValor ativoValor;
    @NotNull(message = "Campo 'dataMovimento' é obrigatório")
    private LocalDate dataMovimento;
    @DecimalMin(value = "0.01", message = "Campo 'quantidade' inválido")
    @Digits(integer = 8, fraction = 2, message = "Campo 'quantidade' inválido")
    @NotNull(message = "Campo 'quantidade' é obrigatório")
    private BigDecimal quantidade;
    @DecimalMin(value = "0.01", message = "Campo 'valor' inválido")
    @Digits(integer = 15, fraction = 0, message = "Campo 'valor' inválido")
    @NotNull(message = "Campo 'valor' é obrigatório")
    private BigDecimal valor;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Campo 'tipoMovimento'é obrigatório")
    private TipoMovimento tipoMovimento;
}
