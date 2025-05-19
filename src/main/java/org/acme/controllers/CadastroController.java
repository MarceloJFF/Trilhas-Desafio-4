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
import org.acme.models.Ecoponto;
import org.acme.models.Endereco;
import org.acme.models.Usuario;
import org.acme.repositories.AcessoRepository;
import org.acme.repositories.EcopontoRepository;
import org.acme.repositories.EnderecoRepository;
import org.acme.repositories.UsuarioRepository;
import org.acme.util.PasswordUtil;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

@Path("/cadastro")
public class CadastroController {
    @Inject
    Template Cadastro_Ecoponto;

    @Inject
    Template login;

    @Inject
    EcopontoRepository ecopontoRepository;
    @Inject
    AcessoRepository acessoRepository;
    @Inject
    PasswordUtil passwordUtil;
    @Inject
    EnderecoRepository enderecoRepository;
    @Inject
    UsuarioRepository usuarioRepository;

    @POST
    @Path("Ecoponto")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    @Transactional
    public Object cadastrarEcoponto(@FormParam("descricao") String descricao,
                                    @FormParam("latitude") BigDecimal latitude,
                                    @FormParam("longitude") BigDecimal longitude,
                                    @FormParam("cep") String cep,
                                    @FormParam("logradouro") String logradouro,
                                    @FormParam("bairro") String bairro,
                                    @FormParam("complemento") String complemento,
                                    @FormParam("login") String login,
                                    @FormParam("password") String password) {
        Optional<Acesso> existente = acessoRepository.find("login", login).firstResultOptional();

        if (existente.isPresent()) {
            // Retorna à página de cadastro com mensagem de erro
            return Cadastro_Ecoponto.data("erro", "Login já está em uso. Escolha outro.");
        }

        try {
            Acesso acesso = new Acesso();
            acesso.setLogin(login);
            acesso.setPassword(PasswordUtil.hash(password));
            acessoRepository.persist(acesso);

            Endereco endereco = new Endereco();
            endereco.setBairro(bairro);
            endereco.setComplemento(complemento);
            endereco.setLogradouro(logradouro);
            enderecoRepository.persist(endereco);

            Ecoponto ecoponto = new Ecoponto();
            ecoponto.setDescricao(descricao);
            ecoponto.setCep(cep);
            ecoponto.setAcesso(acesso);
            ecoponto.setLatitude(latitude);
            ecoponto.setLongitude(longitude);
            ecoponto.setEndereco(endereco);
            ecoponto.setAceitaLixoEletronico(true);
            ecopontoRepository.persist(ecoponto);

            // Redireciona para o login com mensagem de sucesso via query string
            URI loginUri = UriBuilder.fromPath("/login").queryParam("sucesso", "true").build();
            return Response.seeOther(loginUri).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Cadastro_Ecoponto.data("erro", "Erro ao cadastrar Ecoponto. Tente novamente.");
        }
    }


    @POST
    @Path("Usuario")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public Object cadastrarUsuario(@FormParam("login") String login,
                                   @FormParam("password") String password,
                                   @FormParam("primeiro-nome") String primeiroNome,
                                   @FormParam("ultimo-nome") String ultimoNome,
                                   @FormParam("cpf") String cpf,
                                   @FormParam("cep") String cep,
                                   @FormParam("logradouro") String logradouro,
                                   @FormParam("bairro") String bairro,
                                   @FormParam("complemento") String complemento) {
        Optional<Acesso> existente = acessoRepository.find("login", login).firstResultOptional();

        if (existente.isPresent()) {
            // Retorna à página de cadastro com mensagem de erro
            return Cadastro_Ecoponto.data("erro", "Login já está em uso. Escolha outro.");
        }

        try {
            Acesso acesso = new Acesso();
            acesso.setLogin(login);
            acesso.setPassword(PasswordUtil.hash(password));
            acessoRepository.persist(acesso);

            Endereco endereco = new Endereco();
            endereco.setBairro(bairro);
            endereco.setComplemento(complemento);
            endereco.setLogradouro(logradouro);
            enderecoRepository.persist(endereco);

            Usuario usuario = new Usuario();
            usuario.setCpf(cpf);
            usuario.setPrimerioNome(primeiroNome);
            usuario.setUltimoNome(ultimoNome);
            usuario.setEndereco(endereco);
            usuario.setAcesso(acesso);
            usuarioRepository.persist(usuario);

            // Redireciona para o login com mensagem de sucesso
            URI loginUri = UriBuilder.fromPath("/login/empresa").queryParam("sucesso", "true").build();
            return Response.seeOther(loginUri).build();
        } catch (Exception e) {
            return Cadastro_Ecoponto.data("erro", "Erro ao cadastrar usuário. Tente novamente.");
        }
    }

    @GET
    @Path("/cadastro/ecoponto")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance cadastroEcoponto(@QueryParam("erro") String erro) {
        String mensagemErro = null;
        if ("login".equals(erro)) {
            mensagemErro = "Login já está em uso.";
        } else if ("geral".equals(erro)) {
            mensagemErro = "Erro ao cadastrar. Tente novamente.";
        }
        return Cadastro_Ecoponto.data("erro", mensagemErro).data("sucesso",false);
    }


}
