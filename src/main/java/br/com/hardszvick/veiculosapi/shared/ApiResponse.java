package br.com.hardszvick.veiculosapi.shared;

public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
}