package org.acme.controllers;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.models.Acesso;
import org.acme.models.Ecoponto;
import org.acme.models.Endereco;
import org.acme.models.Usuario;
import org.acme.repositories.AcessoRepository;
import org.acme.repositories.EcopontoRepository;
import org.acme.repositories.EnderecoRepository;
import org.acme.repositories.UsuarioRepository;

import java.math.BigDecimal;
import java.net.URI;

@Path("/cadastro")
public class CadastroController {
    @Inject
    EcopontoRepository ecopontoRepository;
    @Inject
    AcessoRepository acessoRepository;
    @Inject
    EnderecoRepository enderecoRepository;
    @Inject
    UsuarioRepository usuarioRepository;

    @POST
    @Path("Ecoponto")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response cadastrarEcoponto(@FormParam("descricao") String descricao,
                                      @FormParam("latitude") BigDecimal latitude,
                                      @FormParam("longitude") BigDecimal longitude,
                                      @FormParam("cep") String cep,
                                      @FormParam("logradouro") String logradouro,
                                      @FormParam("bairro") String bairro,
                                      @FormParam("complemento") String complemento,
                                      @FormParam("login") String login,
                                      @FormParam("password") String password) {

        Acesso acesso = new Acesso();
        acesso.setLogin(login);
        acesso.setPassword(password);
        //verificar se ja exits algum login com esse
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
        ecopontoRepository.persist(ecoponto);
        return Response.seeOther(URI.create("/login/empresa")).build();
    }

    public Response cadastrarUsuario(@FormParam("login") String login,
                                     @FormParam("password") String password,
                                     @FormParam("primeiro-nome") String primeiroNome,
                                     @FormParam("ultimo-nome") String ultimoNome,
                                     @FormParam("cpf") String cpf,
                                     @FormParam("cep") String cep,
                                     @FormParam("logradouro") String logradouro,
                                     @FormParam("bairro") String bairro,
                                     @FormParam("complemento") String complemento
    ) {
        Acesso acesso = new Acesso();
        acesso.setLogin(login);
        acesso.setPassword(password);
        //verificar acesso
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
        return Response.seeOther(URI.create("/login/empresa")).build();
    }    public Response cadastrarEmpresa(@FormParam("login") String login,
                                     @FormParam("password") String password,
                                     @FormParam("nome") String nome,
                                     @FormParam("cnpj") String cnpj,
                                     @FormParam("contato") String contato,
                                     @FormParam("cep") String cep,
                                     @FormParam("logradouro") String logradouro,
                                     @FormParam("bairro") String bairro,
                                     @FormParam("complemento") String complemento
    ) {
        Acesso acesso = new Acesso();
        acesso.setLogin(login);
        acesso.setPassword(password);
        //verificar acesso
        Endereco endereco = new Endereco();
        endereco.setBairro(bairro);
        endereco.setComplemento(complemento);
        endereco.setLogradouro(logradouro);
        enderecoRepository.persist(endereco);

        return Response.seeOther(URI.create("/login/empresa")).build();
    }



}
