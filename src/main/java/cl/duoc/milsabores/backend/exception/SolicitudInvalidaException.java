package cl.duoc.milsabores.backend.exception;

public class SolicitudInvalidaException
        extends RuntimeException {

    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }
}