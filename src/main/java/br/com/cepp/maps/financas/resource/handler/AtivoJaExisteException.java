package br.com.cepp.maps.financas.resource.handler;

public class AtivoJaExisteException extends ObjetoJaExisteException {
    public AtivoJaExisteException(String codigo) {
        super(String.format("Já existe um Ativo com o código %s", codigo));
    }
}
