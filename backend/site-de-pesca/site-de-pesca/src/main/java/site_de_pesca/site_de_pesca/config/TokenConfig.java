package site_de_pesca.site_de_pesca.config;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import site_de_pesca.site_de_pesca.entities.User;

@Component
public class TokenConfig {
    private final String secret = "secret";

    Algorithm algorithm = Algorithm.HMAC256(secret);

    public String generateToken(User user) {
        return JWT.create()
        .withClaim("userId", user.getId())
        .withSubject(user.getEmail())
        .withExpiresAt(Instant.now().plusSeconds(86400))
        .withIssuedAt(Instant.now())
        .sign(algorithm);
    }

    public Optional<JWTUserData> validateToken(String token) {

        try {
        DecodedJWT decode = JWT.require(algorithm).build().verify(token); 

        return Optional.of(JWTUserData.builder()
        .userId(decode.getClaim("userId").asLong())
        .email(decode.getSubject()).build());

        } catch (JWTVerificationException ex) {
            return Optional.empty();
        }
    }
}
