package br.com.hardszvick.veiculosapi.controller;

import br.com.hardszvick.veiculosapi.service.AuthService;
import br.com.hardszvick.veiculosapi.shared.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class LogoutController {
    private final AuthService services;

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader("Authorization") String authHeader
    ) {
        String accessToken = authHeader.startsWith("Bearer ")
                ? authHeader.substring(7)
                : authHeader;

        this.services.logoff(accessToken);

        return ResponseEntity.ok(new ApiResponse<>(true, "Sessão encerrada com sucesso", null));
    }
}
