package br.com.hardszvick.veiculosapi.controller;

import br.com.hardszvick.veiculosapi.dto.request.RefreshRequest;
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
public class RefreshController {
    private final AuthService services;

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refresh(
            @Valid @RequestBody RefreshRequest request
    ) {
        TokenResponse token = this.services.refresh(request.refreshToken());

        return ResponseEntity.ok(new ApiResponse<>(true, null, token));
    }
}
