package br.com.hardszvick.veiculosapi.controller;

import br.com.hardszvick.veiculosapi.dto.request.RegisterRequest;
import br.com.hardszvick.veiculosapi.dto.response.RegisterResponse;
import br.com.hardszvick.veiculosapi.entity.User;
import br.com.hardszvick.veiculosapi.service.AuthService;
import br.com.hardszvick.veiculosapi.shared.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class RegisterController {
    private final AuthService services;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        String login = request.login();
        String password = request.password();

        User user = this.services.register(login, password);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResponse<>
                        (true,
                                null,
                                new RegisterResponse(user.getId(),
                                        login)
                        )
                );
    }
}
