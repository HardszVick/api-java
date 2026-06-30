package br.com.hardszvick.veiculosapi.dto.response;

public record TokenResponse(
        TokenExpiresInResponse refreshToken,
        TokenExpiresInResponse accessToken
) {}
