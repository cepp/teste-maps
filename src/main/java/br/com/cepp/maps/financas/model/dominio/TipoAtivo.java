package br.com.cepp.maps.financas.model.dominio;

import lombok.Getter;

public enum TipoAtivo {
    RV("RENDA VARIÁVEL"), RF("RENDA FIXA"), FUNDO("FUNDO INVESTIMENTO");

    @Getter
    final String descricao;

    TipoAtivo(String descricao) {
        this.descricao = descricao;
    }
}
