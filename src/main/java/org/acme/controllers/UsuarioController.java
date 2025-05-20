package org.acme.controllers;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("usuario")
public class UsuarioController {
    
    @GET
    @Path("/perfil")
    public Response getPerfil() {
        // Apenas usuários com role "usuario" podem acessar
        return Response.ok("Perfil do usuário").build();
    }
} 