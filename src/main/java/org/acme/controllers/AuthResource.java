package org.acme.controllers;

import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.acme.dto.AcessoDTO;
import org.acme.models.Acesso;
import org.acme.repositories.AcessoRepository;
import org.acme.util.PasswordUtil;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Path("/auth")
public class AuthResource {
    @Inject
    AcessoRepository acessoRepository;
    @Inject
    JsonWebToken jwt;

    @POST
    @Path("/login")
    public Response login(AcessoDTO acesso){
        Acesso acessoModel = acessoRepository.findByLogin(acesso.getLogin());
        
        if (acessoModel != null && PasswordUtil.check(acesso.getPassword(), acessoModel.getPassword())) {
            Set<String> groups = new HashSet<>();
            groups.add(acessoModel.getTipo());
            String token = Jwt.issuer("https://trilhas-desafio-4-production-890a.up.railway.app")
                    .upn(acesso.getLogin())
                    .groups(groups)
                    .expiresIn(Duration.ofMinutes(5)) // ✅ Tempo de expiração reduzido
                    .sign();
            return Response.ok().entity(token).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("/verify")
    public Response verifyToken() {
        String username = jwt.getSubject(); // ou jwt.getClaim(Claims.upn)
        Set<String> groups = jwt.getGroups();
        return Response.ok()
                .entity("Token válido para o usuário: " + username + ", grupos: " + groups)
                .build();
    }
}
