package org.acme.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.LoginDTO;
import org.acme.dto.TokenResponseDTO;
import org.acme.entities.Login;
import org.acme.repositories.LoginRepository;
import org.acme.security.JwtTokenProvider;
import org.acme.utils.PasswordUtil;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {

    @Inject
    LoginRepository loginRepository;

    @Inject
    JwtTokenProvider jwtTokenProvider;

    @POST
    @Transactional
    public Response handleLogin(LoginDTO loginDTO) {
        Login login = loginRepository.findByLogin(loginDTO.getLogin());

        if (login == null || !PasswordUtil.check(loginDTO.getPassword(), login.getPassword())) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Login ou senha inválidos.")
                    .build();
        }

        String token = jwtTokenProvider.generateToken(login.getLogin(), login.getTipo());
        return Response.ok(new TokenResponseDTO(token, login.getTipo(), login.getLogin())).build();
    }
}

