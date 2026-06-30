package br.com.hardszvick.veiculosapi.Exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("Usuário já existe");
    }
}
