package br.com.hardszvick.veiculosapi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Builder
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Builder.Default
    @Column(name = "revoked", nullable = false)
    private Boolean revoked = false;

    @Column(name = "id_user", nullable = false)
    private UUID idUser;
}
