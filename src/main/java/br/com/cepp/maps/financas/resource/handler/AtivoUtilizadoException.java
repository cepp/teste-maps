package br.com.cepp.maps.financas.resource.handler;

public class AtivoUtilizadoException extends RuntimeException {
    public AtivoUtilizadoException(String codigo) {
        super(String.format("Ativo '%s' não pode ser excluído, porque está sendo utilizado", codigo));
    }
}
