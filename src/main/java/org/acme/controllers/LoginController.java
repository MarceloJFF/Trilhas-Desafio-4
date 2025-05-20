package org.acme.controllers;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.acme.models.Acesso;
import org.acme.repositories.AcessoRepository;
import org.acme.util.PasswordUtil;

import java.net.URI;

@Path("/login")
public class LoginController {

    @Inject
    AcessoRepository acessoRepository;

    @Inject
    Template login; // injeta template login.qute.html

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Object handleLogin(@FormParam("login") String loginInput,
                                        @FormParam("password") String password) {

        Acesso acesso = acessoRepository.findByLogin(loginInput);

        if (acesso == null || !PasswordUtil.check(password, acesso.getPassword())) {
            return login.data("erro", "Login ou senha inválidos.").data("sucesso",false);
        }

        if (acesso.getUsuario() != null) {
            //return TemplateInstance.redirect("/usuario");
            URI loginUri = UriBuilder.fromPath("/usuario").build();
            return Response.seeOther(loginUri).build();
        } else if (acesso.getEcoponto() != null) {
            URI loginUri = UriBuilder.fromPath("eco-gerenciar").build();
            return Response.seeOther(loginUri).build();
        }
        return login.data("erro", "Tipo de usuário não reconhecido.");
    }
//    @GET
//    @Path("/login")
//    @Produces(MediaType.TEXT_HTML)
//    public TemplateInstance loginEmpresa(@QueryParam("sucesso") boolean sucesso) {
//        return login.data("sucesso", sucesso);
//    }

}

