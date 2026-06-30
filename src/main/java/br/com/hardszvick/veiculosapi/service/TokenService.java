package br.com.hardszvick.veiculosapi.service;

import br.com.hardszvick.veiculosapi.entity.Token;
import br.com.hardszvick.veiculosapi.entity.User;
import br.com.hardszvick.veiculosapi.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final TokenRepository repo;

    public String generateAccessToken(User user) {
        int secondsToAwait = 900;

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth-api")
                .subject(user.getId().toString())
                .claim("userId", user.getId())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(secondsToAwait))
                .id(UUID.randomUUID().toString())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public String generateRefreshToken() {
        byte[] bytes = new byte[32];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public Optional<Token> getTokenByRefreshToken(String refreshToken) {
        return this.repo.findByRefreshToken(refreshToken)
                .filter(t -> !t.getRevoked());
    }

    public void revoke(String accessToken) {
        this.repo.revokeByAccessToken(accessToken);
    }

    public void save(Token token) {
        this.repo.save(token);
    }
}
