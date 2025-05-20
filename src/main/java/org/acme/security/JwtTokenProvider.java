package org.acme.security;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashSet;

@ApplicationScoped
public class JwtTokenProvider {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    @ConfigProperty(name = "quarkus.smallrye-jwt.sign.key.location")
    String keyLocation;

    public String generateToken(String login, String tipo) {
        return Jwt.issuer(issuer)
                .subject(login)
                .groups(new HashSet<>(Arrays.asList(tipo)))
                .expiresIn(Duration.ofHours(24))
                .sign();
    }

    public String getLoginFromToken(String token) {
        return Jwt.parser().parse(token).getSubject();
    }

    public String getTipoFromToken(String token) {
        return Jwt.parser().parse(token).getGroups().iterator().next();
    }
} 