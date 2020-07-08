package br.com.cepp.maps.financas.resource.handler;

public class ConversaoParaJsonException extends RuntimeException {
    public ConversaoParaJsonException(String toStringObjeto) {
        super(String.format("Falha ao converter objeto em JSON: %s", toStringObjeto));
    }
}
