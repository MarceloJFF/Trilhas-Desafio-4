package org.acme.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CadastroEcopontoDTO;
import org.acme.dto.CadastroUsuarioDTO;
import org.acme.models.Acesso;
import org.acme.models.Ecoponto;
import org.acme.models.Endereco;
import org.acme.models.Usuario;
import org.acme.repositories.*;
import org.acme.util.PasswordUtil;
@Path("/cadastro")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CadastroController {

    @Inject
    UsuarioRepository usuarioRepository;



    @Inject
    EcopontoRepository ecopontoRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    AcessoRepository acessoRepository;

    @POST
    @Path("/usuario")
    @Transactional
    public Response cadastrarUsuario(CadastroUsuarioDTO dto) {
        if (acessoRepository.findByLogin(dto.getLogin()) != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Login já existe")
                    .build();
        }
        System.out.print("entrou aqui");
        System.out.println(dto);

        Endereco endereco = new Endereco();
        endereco.setCep(dto.getCep());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setBairro(dto.getBairro());
        endereco.setComplemento(dto.getComplemento());
        enderecoRepository.persist(endereco);

        Acesso acesso = new Acesso();
        acesso.setLogin(dto.getLogin());
        acesso.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        acesso.setTipo("usuario");
        acessoRepository.persist(acesso);

        Usuario usuario = new Usuario();
        usuario.setPrimeiroNome(dto.getPrimeiroNome());
        usuario.setUltimoNome(dto.getUltimoNome());
        usuario.setCpf(dto.getCpf());
        usuario.setEndereco(endereco);
        usuario.setAcesso(acesso);
        usuarioRepository.persist(usuario);


        return Response.ok(usuario).build();
    }

    @POST
    @Path("/ecoponto")
    @Transactional
    public Response cadastrarEcoponto(CadastroEcopontoDTO dto) {
        if (acessoRepository.findByLogin(dto.getLogin()) != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Login já existe")
                    .build();
        }

        Endereco endereco = new Endereco();
        endereco.setCep(dto.getCep());
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setBairro(dto.getBairro());
        endereco.setComplemento(dto.getComplemento());
        enderecoRepository.persist(endereco);

        Acesso acesso = new Acesso();
        acesso.setLogin(dto.getLogin());
        acesso.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        acesso.setTipo("ecoponto");
        acessoRepository.persist(acesso);

        Ecoponto ecoponto = new Ecoponto();
        ecoponto.setDescricao(dto.getDescricao());
        ecoponto.setLatitude(dto.getLatitude());
        ecoponto.setLongitude(dto.getLongitude());
        ecoponto.setCep(dto.getCep());
        ecoponto.setEndereco(endereco);
        ecoponto.setAcesso(acesso);
        ecopontoRepository.persist(ecoponto);

        return Response.ok(ecoponto).build();
    }
}
