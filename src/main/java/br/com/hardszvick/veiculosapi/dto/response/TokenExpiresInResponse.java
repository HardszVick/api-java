package br.com.hardszvick.veiculosapi.dto.response;

public record TokenExpiresInResponse(
        String token,
        Long expiresIn
) {
}