package org.acme.security;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    @jakarta.ws.rs.core.Context
    JsonWebToken jwt;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token não fornecido")
                    .build());
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        try {
            // A validação do token é feita automaticamente pelo Quarkus
            // Se chegou aqui, o token é válido
            String tipo = jwt.getGroups().iterator().next();
            requestContext.setProperty("tipo", tipo);
            requestContext.setProperty("login", jwt.getSubject());
        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token inválido")
                    .build());
        }
    }
} 