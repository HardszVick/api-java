package br.com.hardszvick.veiculosapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Login é obrigatório")
        @Size(min = 3, max = 100, message = "Login deve ter entre 3 e 100 caracteres")
        String login,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, max = 128, message = "Senha deve ter entre 8 e 128 caracteres")
        String password
) {}
