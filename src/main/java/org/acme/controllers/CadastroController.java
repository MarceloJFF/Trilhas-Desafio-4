package org.acme.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.CadastroEcopontoDTO;
import org.acme.dto.CadastroUsuarioDTO;
import org.acme.dto.CadastroEmpresaDTO;
import org.acme.entities.*;
import org.acme.repositories.*;
import org.acme.utils.PasswordUtil;

@Path("/cadastro")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CadastroController {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    EmpresaRepository empresaRepository;

    @Inject
    EcopontoRepository ecopontoRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    @Inject
    LoginRepository loginRepository;

    @POST
    @Path("/usuario")
    @Transactional
    public Response cadastrarUsuario(CadastroUsuarioDTO dto) {
        if (loginRepository.findByLogin(dto.getLogin()) != null) {
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

        Login login = new Login();
        login.setLogin(dto.getLogin());
        login.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        login.setTipo("usuario");
        loginRepository.persist(login);

        Usuario usuario = new Usuario();
        usuario.setPrimeiroNome(dto.getPrimeiroNome());
        usuario.setUltimoNome(dto.getUltimoNome());
        usuario.setCpf(dto.getCpf());
        usuario.setEndereco(endereco);
        usuario.setLogin(login);
        usuarioRepository.persist(usuario);

        return Response.ok(usuario).build();
    }

    @POST
    @Path("/empresa")
    @Transactional
    public Response cadastrarEmpresa(CadastroEmpresaDTO dto) {
        if (loginRepository.findByLogin(dto.getLogin()) != null) {
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

        Login login = new Login();
        login.setLogin(dto.getLogin());
        login.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        login.setTipo("empresa");
        loginRepository.persist(login);

        Empresa empresa = new Empresa();
        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());
        empresa.setEndereco(endereco);
        empresa.setLogin(login);
        empresaRepository.persist(empresa);

        return Response.ok(empresa).build();
    }

    @POST
    @Path("/ecoponto")
    @Transactional
    public Response cadastrarEcoponto(CadastroEcopontoDTO dto) {
        if (loginRepository.findByLogin(dto.getLogin()) != null) {
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

        Login login = new Login();
        login.setLogin(dto.getLogin());
        login.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
        login.setTipo("ecoponto");
        loginRepository.persist(login);

        Ecoponto ecoponto = new Ecoponto();
        ecoponto.setDescricao(dto.getDescricao());
        ecoponto.setLatitude(dto.getLatitude());
        ecoponto.setLongitude(dto.getLongitude());
        ecoponto.setEndereco(endereco);
        ecoponto.setLogin(login);
        ecopontoRepository.persist(ecoponto);

        return Response.ok(ecoponto).build();
    }
}
