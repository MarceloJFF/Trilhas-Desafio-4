package org.acme.controllers.Auth.ecoponto;

import org.acme.models.Acesso;
import org.acme.models.Usuario;
import org.acme.repositories.AcessoRepository;
import org.acme.repositories.UsuarioRepository;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("usuario")
public class UsuarioController {
     
    @Inject
    AcessoRepository acessoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @GET
    @Path("/perfil")
    public Response getPerfil(@QueryParam("login") String login) {
        // Apenas usuários com role "usuario" podem acessar
        Acesso acesso = acessoRepository.findByLogin(login);
        if (acesso == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado")
                    .build();
        }
        Usuario usuario = usuarioRepository.findByAcessoId(acesso.getId());
        
        return Response.ok(usuario).build();
    }
} 