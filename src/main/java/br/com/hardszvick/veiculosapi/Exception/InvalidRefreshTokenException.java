package br.com.hardszvick.veiculosapi.Exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Refresh token inválido ou expirado");
    }
}
