package org.acme.controllers.Telas;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/")
public class TelaController {

    @Inject
    Template login;
    @Inject
    Template tela_usuario;
    @Inject
    Template Cadastro_Usuario;
    @Inject
    Template Cadastro_Beneficio;
    @Inject
    Template Cadastro_Ecoponto;
    @Inject
    Template Beneficio_Usuario;
    @Inject
    Template Eco_Gerenciar;
    @Inject
    Template conf_Ecoponto;
    @Inject
    Template eventos;

    @GET
    @Path("login")
    public TemplateInstance renderLogin() {
        return login.data("sucesso",false).data("erro",false);
    }

    @GET
    @Path("usuario")
    public TemplateInstance renderTelaUsuario() {
        return tela_usuario.instance();
    }

    @GET
    @Path("cadastro-usuario")
    public TemplateInstance renderCadastroUsuario() {
        return Cadastro_Usuario.instance();
    }

    @GET
    @Path("cadastro-beneficio")
    public TemplateInstance renderCadastroBeneficio() {
        return Cadastro_Beneficio.instance();
    }

    @GET
    @Path("cadastro-ecoponto")
    public TemplateInstance renderCadastroEcoponto() {
        return Cadastro_Ecoponto.data("erro",false);
    }

    @GET
    @Path("beneficio-usuario")
    public TemplateInstance renderBeneficioUsuario() {
        return Beneficio_Usuario.instance();
    }

    @GET
    @Path("eco-gerenciar")
    public TemplateInstance renderEcoGerenciar() {
        return Eco_Gerenciar.instance();
    }

    @GET
    @Path("conf-ecoponto")
    public TemplateInstance renderConfEcoponto() {
        return conf_Ecoponto.data("success",false);
    }

    @GET
    @Path("eventos")
    public TemplateInstance renderEventos() {
        return eventos.instance();
    }
}
