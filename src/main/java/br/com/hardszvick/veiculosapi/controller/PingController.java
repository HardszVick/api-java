package br.com.hardszvick.veiculosapi.controller;

import br.com.hardszvick.veiculosapi.shared.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class PingController {
    @GetMapping("/ping")
    public ResponseEntity<ApiResponse<String>> ping() {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Pong",
                        null
                )
        );
    }
}
