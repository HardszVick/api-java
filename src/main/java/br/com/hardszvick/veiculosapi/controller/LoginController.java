package br.com.hardszvick.veiculosapi.controller;

import br.com.hardszvick.veiculosapi.dto.request.LoginRequest;
import br.com.hardszvick.veiculosapi.dto.response.TokenResponse;
import br.com.hardszvick.veiculosapi.service.AuthService;
import br.com.hardszvick.veiculosapi.shared.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
    private final AuthService services;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        String login = request.login();
        String password = request.password();
        TokenResponse token = this.services.login(login, password);

        return ResponseEntity.ok(
                new ApiResponse<>(true,
                        null,
                        token)
        );
    }
}
