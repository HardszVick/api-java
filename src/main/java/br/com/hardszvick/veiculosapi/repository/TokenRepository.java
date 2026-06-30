package br.com.hardszvick.veiculosapi.repository;

import br.com.hardszvick.veiculosapi.entity.Token;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    Optional<Token> findByRefreshToken(String refreshToken);

    @Modifying
    @Transactional
    @Query("UPDATE Token t SET t.revoked = true WHERE t.accessToken = :accessToken")
    void revokeByAccessToken(@Param("accessToken") String accessToken);
}
