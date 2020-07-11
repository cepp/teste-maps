package br.com.cepp.maps.financas.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(exclude = {"movimentos"})
@ToString(exclude = {"movimentos"})
@Entity
@Immutable
public class AtivoValor implements Serializable {
    private static final long serialVersionUID = 2001890995695664392L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Campo 'data' é obrigatório")
    private LocalDate data;
    @Digits(integer = 8, fraction = 8, message = "Campo 'valor' inválido")
    @NotNull(message = "Campo 'valor' é obrigatório")
    private BigDecimal valor;
    @NotNull(message = "Campo 'ativo' é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    private Ativo ativo;
    @OneToMany(mappedBy = "ativoValor", fetch = FetchType.LAZY)
    private List<Movimento> movimentos;

}
