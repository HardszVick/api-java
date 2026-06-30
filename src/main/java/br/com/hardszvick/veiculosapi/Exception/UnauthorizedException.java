package br.com.hardszvick.veiculosapi.Exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Não autorizado");
    }
}
