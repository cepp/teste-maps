package br.com.cepp.maps.financas.resource.handler;

public class AtivoJaExisteException extends RuntimeException {
    public AtivoJaExisteException(String codigo) {
        super(String.format("Já existe um Ativo com o código %s", codigo));
    }
}
