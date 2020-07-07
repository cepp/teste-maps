package br.com.cepp.maps.financas.model.dominio;

public enum TipoNatureza {
    CREDITO, DEBITO;

    public boolean isDebito() {
        return DEBITO.equals(this);
    }
}
