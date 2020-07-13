package br.com.cepp.maps.financas.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Immutable;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "usuario")
@Immutable
public class Usuario implements Serializable {
    @Id
    @NotEmpty(message = "Campo 'username' é obrigatório")
    @Column(name = "username")
    private final String username;
    @NotEmpty(message = "Campo 'password' é obrigatório")
    private final String password;
    private final boolean enabled;
    @ElementCollection
    @CollectionTable(
            name = "authorities",
            joinColumns=@JoinColumn(name = "username", referencedColumnName = "username")
    )
    @NotEmpty(message = "Campo 'perfis' é obrigatório")
    private final List<String> perfis;

    private Usuario() {
        this.username = null;
        this.password = null;
        this.enabled = false;
        this.perfis = null;
    }
}
