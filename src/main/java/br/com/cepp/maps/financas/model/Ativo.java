package br.com.cepp.maps.financas.model;

import br.com.cepp.maps.financas.model.dominio.TipoAtivo;
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
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
@Entity
@Immutable
public class Ativo implements Serializable {
    private static final long serialVersionUID = -5844084754339681103L;

    @Id
    @NotEmpty(message = "Campo 'codigo' é obrigatório")
    private String codigo;
    @NotEmpty(message = "Campo 'nome' é obrigatório")
    private String nome;
    @NotNull(message = "Campo 'tipoAtivo' é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoAtivo tipoAtivo;
    @NotNull(message = "Campo 'dataEmissao' é obrigatório")
    private LocalDate dataEmissao;
    @NotNull(message = "Campo 'dataVencimento' é obrigatório")
    private LocalDate dataVencimento;

    public Ativo comCodigo(final String codigo) {
        return new Ativo(codigo, this.nome, this.tipoAtivo, this.dataEmissao, this.dataVencimento);
    }

    public Ativo comNome(final String nome) {
        return new Ativo(this.codigo, nome, this.tipoAtivo, this.dataEmissao, this.dataVencimento);
    }

    public Ativo comTipoAtivo(final TipoAtivo tipoAtivo) {
        return new Ativo(this.codigo, this.nome, tipoAtivo, this.dataEmissao, this.dataVencimento);
    }
}
