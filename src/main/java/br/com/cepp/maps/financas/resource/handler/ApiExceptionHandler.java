package br.com.cepp.maps.financas.resource.handler;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  @NotNull HttpHeaders headers, @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        if (ex.getCause() instanceof UnrecognizedPropertyException) {
            String nomePropriedade = ((UnrecognizedPropertyException) ex.getCause()).getPropertyName();
            return super.handleExceptionInternal(ex, String.format("Campo '%s' não reconhecido", nomePropriedade), headers, HttpStatus.BAD_REQUEST, request);
        }

        return super.handleExceptionInternal(ex, ex.getCause().getMessage(), headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        List<String> erros = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> erros.add(fieldError.getDefaultMessage()));

        if (ex.getBindingResult().getFieldErrors().isEmpty()) {
            ex.getBindingResult().getAllErrors().forEach(erro -> erros.add(erro.getDefaultMessage()));
        }

        return super.handleExceptionInternal(ex, erros.toString(), headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Object> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ObjetoNaoEncontradoException.class)
    public ResponseEntity<Object> handleObjetoNaoEncontradoException(ObjetoNaoEncontradoException ex) {
        return this.getRespostaErroPadrao(HttpStatus.NO_CONTENT, ex.getMessage());
    }

    @ExceptionHandler(ObjetoJaExisteException.class)
    public ResponseEntity<Object> handleObjetoJaExisteException(ObjetoJaExisteException ex) {
        return this.getRespostaErroPadrao(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ValidacaoNegocioException.class)
    public ResponseEntity<Object> handleValidacaoNegocioException(ValidacaoNegocioException ex) {
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AtivoPeriodoInvalidoException.class)
    public ResponseEntity<Object> handleAtivoPeriodoInvalidoException(AtivoPeriodoInvalidoException ex) {
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MovimentoFinalSemanaException.class)
    public ResponseEntity<Object> handleMoviemntoFinalSemanaException(MovimentoFinalSemanaException ex) {
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(AtivoValorUtilizadoException.class)
    public ResponseEntity<Object> handleAtivoValorUtilizadoException(AtivoValorUtilizadoException ex) {
        return this.getRespostaErroPadrao(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(AtivoUtilizadoException.class)
    public ResponseEntity<Object> handleAtivoUtilizadoException(AtivoUtilizadoException ex) {
        return this.getRespostaErroPadrao(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MovimentoNaoEcontradoException.class)
    public ResponseEntity<Object> handleMovimentoNaoEcontradoException(MovimentoNaoEcontradoException ex) {
        return this.getRespostaErroPadrao(HttpStatus.NO_CONTENT, ex.getMessage());
    }

    private ResponseEntity<Object> getRespostaErroPadrao(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(message);
    }
}

