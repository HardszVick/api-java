package br.com.hardszvick.veiculosapi.service;

import br.com.hardszvick.veiculosapi.Exception.InvalidCredentialsException;
import br.com.hardszvick.veiculosapi.Exception.UnauthorizedException;
import br.com.hardszvick.veiculosapi.Exception.UserAlreadyExistsException;
import br.com.hardszvick.veiculosapi.Exception.UserNotFound;
import br.com.hardszvick.veiculosapi.dto.response.TokenExpiresInResponse;
import br.com.hardszvick.veiculosapi.dto.response.TokenResponse;
import br.com.hardszvick.veiculosapi.entity.Token;
import br.com.hardszvick.veiculosapi.entity.User;
import br.com.hardszvick.veiculosapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repo;
    private final TokenService token;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResponse login(String login, String password) {
        User u = this.repo.findByLogin(login)
                .orElseThrow(InvalidCredentialsException::new);

        if (!this.passwordEncoder.matches(password, u.getPassword()))
            throw new InvalidCredentialsException();

        log.info("Login realizado: {}", login);
        return this.generateTokens(u);
    }

    @Transactional
    public TokenResponse refresh(String refreshToken) {
        Token t = this.token.getTokenByRefreshToken(refreshToken)
                .orElseThrow(UnauthorizedException::new);
        User u = this.repo.findById(t.getIdUser())
                .orElseThrow(UserNotFound::new);

        log.info("Tokens renovados para usuário: {}", u.getId());
        return this.generateTokens(u);
    }

    @Transactional
    public User register(String login, String password) {
        if (this.repo.findByLogin(login).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        User user = User.builder()
                .login(login)
                .password(passwordEncoder.encode(password))
                .build();

        log.info("Usuário registrado: {}", login);
        return this.repo.save(user);
    }

    public void logoff(String accessToken) {
        this.token.revoke(accessToken);
        log.info("Token revogado");
    }

    private TokenResponse generateTokens(User user) {
        String hashAccessToken = this.token.generateAccessToken(user);
        String hashRefreshToken = this.token.generateRefreshToken();

        TokenExpiresInResponse accessToken = new TokenExpiresInResponse(hashAccessToken, 900L);
        TokenExpiresInResponse refreshToken = new TokenExpiresInResponse(hashRefreshToken, 86400L);

        Token newToken = new Token()
                .setAccessToken(hashAccessToken)
                .setRefreshToken(hashRefreshToken)
                .setIdUser(user.getId())
                .setRevoked(false);

        this.token.save(newToken);
        return new TokenResponse(refreshToken, accessToken);
    }
}
