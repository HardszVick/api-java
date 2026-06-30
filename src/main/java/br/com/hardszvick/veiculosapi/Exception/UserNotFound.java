package br.com.hardszvick.veiculosapi.Exception;

public class UserNotFound extends RuntimeException {
    public UserNotFound() {
        super("Usuário não encontrado");
    }
}