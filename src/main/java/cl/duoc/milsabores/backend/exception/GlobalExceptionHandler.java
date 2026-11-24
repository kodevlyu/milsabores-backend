package cl.duoc.milsabores.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarNoEncontrado(
            RecursoNoEncontradoException ex,
            HttpServletRequest request) {

        return construirRespuesta(
            HttpStatus.NOT_FOUND,
            ex.getMessage(),
            request,
            Map.of()
        );
    }

    @ExceptionHandler(ConflictoException.class)
    public ResponseEntity<ErrorResponse> manejarConflicto(
            ConflictoException ex,
            HttpServletRequest request) {

        return construirRespuesta(
            HttpStatus.CONFLICT,
            ex.getMessage(),
            request,
            Map.of()
        );
    }

    @ExceptionHandler(SolicitudInvalidaException.class)
    public ResponseEntity<ErrorResponse> manejarSolicitudInvalida(
            SolicitudInvalidaException ex,
            HttpServletRequest request) {

        return construirRespuesta(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            request,
            Map.of()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidaciones(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> detalles = new HashMap<>();

        ex.getBindingResult()
            .getFieldErrors()
            .forEach(error ->
                detalles.put(
                    error.getField(),
                    error.getDefaultMessage()
                )
            );

        return construirRespuesta(
            HttpStatus.BAD_REQUEST,
            "Existen campos invalidos",
            request,
            detalles
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> manejarRestricciones(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        Map<String, String> detalles = new HashMap<>();

        ex.getConstraintViolations()
            .forEach(error ->
                detalles.put(
                    error.getPropertyPath().toString(),
                    error.getMessage()
                )
            );

        return construirRespuesta(
            HttpStatus.BAD_REQUEST,
            "Error de validacion",
            request,
            detalles
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> manejarIntegridad(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        return construirRespuesta(
            HttpStatus.CONFLICT,
            "La operacion incumple una restriccion de datos",
            request,
            Map.of()
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> manejarJsonInvalido(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {

        return construirRespuesta(
            HttpStatus.BAD_REQUEST,
            "El cuerpo de la solicitud no es valido",
            request,
            Map.of()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> manejarTipoInvalido(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        return construirRespuesta(
            HttpStatus.BAD_REQUEST,
            "Parametro invalido: " + ex.getName(),
            request,
            Map.of()
        );
    }

    private ResponseEntity<ErrorResponse> construirRespuesta(
            HttpStatus estado,
            String mensaje,
            HttpServletRequest request,
            Map<String, String> detalles) {

        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            estado.value(),
            estado.getReasonPhrase(),
            mensaje,
            request.getRequestURI(),
            detalles
        );

        return ResponseEntity.status(estado).body(error);
    }
}