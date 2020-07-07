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
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
                                                                     HttpHeaders headers, HttpStatus status,
                                                                     WebRequest request) {
        return super.handleExceptionInternal(ex, "Request incompleto", headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> constraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<String> falhas = new ArrayList<>();
        for (ConstraintViolation<?> violation : violations) {
            falhas.add(violation.getMessage());
        }
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, falhas.toString());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentException(IllegalArgumentException exception) {
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<Object> missingRequestHeaderException(MissingRequestHeaderException ex) {
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, "Header incompleto, está faltando uma informação");
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<Object> handleValidationException(SaldoInsuficienteException ex) {
        return this.getRespostaErroPadrao(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    private ResponseEntity<Object> getRespostaErroPadrao(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(message);
    }
}

