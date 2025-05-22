package org.acme.controllers.Auth.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.EcopontoDTO;
import org.acme.models.Acesso;
import org.acme.models.Ecoponto;
import org.acme.models.Endereco;
import org.acme.repositories.AcessoRepository;
import org.acme.repositories.EcopontoRepository;
import org.acme.repositories.EnderecoRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import org.acme.util.PasswordUtil;
@Path("/ecoponto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EcopontoController {

    @Inject
    EcopontoRepository ecopontoRepository;

    @Inject
    AcessoRepository acessoRepository;

    @Inject
    EnderecoRepository enderecoRepository;

    @GET
    public Response listarTodos() {
        List<Ecoponto> ecopontos = ecopontoRepository.listAll();
        List<EcopontoDTO> ecopontosDTO = ecopontos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Response.ok(ecopontosDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Ecoponto ecoponto = ecopontoRepository.findById(id);
        if (ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }

        // Carregar dados relacionados explicitamente
        if (ecoponto.getAcesso() != null) {
            Acesso acesso = acessoRepository.findById(ecoponto.getAcesso().getId());
            ecoponto.setAcesso(acesso);
        }

        if (ecoponto.getEndereco() != null) {
            Endereco endereco = enderecoRepository.findById(ecoponto.getEndereco().getId());
            ecoponto.setEndereco(endereco);
        }

        return Response.ok(convertToDTO(ecoponto)).build();
    }

    @POST
    @Transactional
    public Response criar(EcopontoDTO ecopontoDTO) {
        // Validar dados obrigatórios
        if (ecopontoDTO.getDescricao() == null || ecopontoDTO.getDescricao().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Descrição é obrigatória")
                    .build();
        }

        if (ecopontoDTO.getLatitude() == null || ecopontoDTO.getLongitude() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Latitude e longitude são obrigatórios")
                    .build();
        }

        if (ecopontoDTO.getCep() == null || ecopontoDTO.getCep().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("CEP é obrigatório")
                    .build();
        }

        // Criar acesso
        Acesso acesso = new Acesso();
        acesso.setLogin(ecopontoDTO.getLogin());
        acesso.setPassword(PasswordUtil.hashPassword(ecopontoDTO.getPassword()));
        acessoRepository.persist(acesso);

        // Criar endereço
        Endereco endereco = new Endereco();
        endereco.setCep(ecopontoDTO.getCep());
        endereco.setLogradouro(ecopontoDTO.getLogradouro());
        endereco.setBairro(ecopontoDTO.getBairro());
        endereco.setComplemento(ecopontoDTO.getComplemento());
        enderecoRepository.persist(endereco);

        // Criar ecoponto
        Ecoponto ecoponto = new Ecoponto();
        ecoponto.setDescricao(ecopontoDTO.getDescricao());
        ecoponto.setLatitude(ecopontoDTO.getLatitude());
        ecoponto.setLongitude(ecopontoDTO.getLongitude());
        ecoponto.setCep(ecopontoDTO.getCep());
        ecoponto.setAceitaLixoEletronico(ecopontoDTO.getAceitaLixoEletronico());
        ecoponto.setAcesso(acesso);
        ecoponto.setEndereco(endereco);

        ecopontoRepository.persist(ecoponto);
        return Response.status(Response.Status.CREATED)
                .entity(convertToDTO(ecoponto))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, EcopontoDTO ecopontoDTO) {
        Ecoponto ecoponto = ecopontoRepository.findById(id);
        if (ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }

        // Atualizar dados do ecoponto
        if (ecopontoDTO.getDescricao() != null && !ecopontoDTO.getDescricao().trim().isEmpty()) {
            ecoponto.setDescricao(ecopontoDTO.getDescricao());
        }

        if (ecopontoDTO.getLatitude() != null) {
            ecoponto.setLatitude(ecopontoDTO.getLatitude());
        }

        if (ecopontoDTO.getLongitude() != null) {
            ecoponto.setLongitude(ecopontoDTO.getLongitude());
        }

        if (ecopontoDTO.getCep() != null && !ecopontoDTO.getCep().trim().isEmpty()) {
            ecoponto.setCep(ecopontoDTO.getCep());
        }

        if (ecopontoDTO.getAceitaLixoEletronico() != null) {
            ecoponto.setAceitaLixoEletronico(ecopontoDTO.getAceitaLixoEletronico());
        }

        // Atualizar dados de acesso
        Acesso acesso = ecoponto.getAcesso();
        if (acesso != null) {
            boolean acessoAtualizado = false;

            if (ecopontoDTO.getLogin() != null && !ecopontoDTO.getLogin().trim().isEmpty()) {
                // Verificar se o novo login já existe para outro ecoponto
                Acesso acessoExistente = acessoRepository.find("login", ecopontoDTO.getLogin()).firstResult();
                if (acessoExistente != null && !acessoExistente.getId().equals(acesso.getId())) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("Login já está em uso por outro ecoponto")
                            .build();
                }
                acesso.setLogin(ecopontoDTO.getLogin());
                acessoAtualizado = true;
            }

            if (ecopontoDTO.getPassword() != null && !ecopontoDTO.getPassword().trim().isEmpty()) {
                acesso.setPassword(PasswordUtil.hashPassword(ecopontoDTO.getPassword()));
                acessoAtualizado = true;
            }

            if (acessoAtualizado) {
                acessoRepository.persist(acesso);
            }
        }

        // Atualizar dados do endereço
        Endereco endereco = ecoponto.getEndereco();
        if (endereco != null) {
            boolean enderecoAtualizado = false;

            if (ecopontoDTO.getLogradouro() != null && !ecopontoDTO.getLogradouro().trim().isEmpty()) {
                endereco.setLogradouro(ecopontoDTO.getLogradouro());
                enderecoAtualizado = true;
            }

            if (ecopontoDTO.getBairro() != null && !ecopontoDTO.getBairro().trim().isEmpty()) {
                endereco.setBairro(ecopontoDTO.getBairro());
                enderecoAtualizado = true;
            }

            if (ecopontoDTO.getComplemento() != null) {
                endereco.setComplemento(ecopontoDTO.getComplemento());
                enderecoAtualizado = true;
            }

            if (ecopontoDTO.getCep() != null && !ecopontoDTO.getCep().trim().isEmpty()) {
                endereco.setCep(ecopontoDTO.getCep());
                enderecoAtualizado = true;
            }

            if (enderecoAtualizado) {
                enderecoRepository.persist(endereco);
            }
        }

        // Persistir as alterações do ecoponto
        ecopontoRepository.persist(ecoponto);

        // Buscar o ecoponto atualizado com todas as relações
        Ecoponto ecopontoAtualizado = ecopontoRepository.findById(id);
        if (ecopontoAtualizado.getAcesso() != null) {
            ecopontoAtualizado.setAcesso(acessoRepository.findById(ecopontoAtualizado.getAcesso().getId()));
        }
        if (ecopontoAtualizado.getEndereco() != null) {
            ecopontoAtualizado.setEndereco(enderecoRepository.findById(ecopontoAtualizado.getEndereco().getId()));
        }

        return Response.ok(convertToDTO(ecopontoAtualizado)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        Ecoponto ecoponto = ecopontoRepository.findById(id);
        if (ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }

        // Remover ecoponto e entidades relacionadas
        Acesso acesso = ecoponto.getAcesso();
        Endereco endereco = ecoponto.getEndereco();

        ecopontoRepository.delete(ecoponto);
        if (acesso != null) {
            acessoRepository.delete(acesso);
        }
        if (endereco != null) {
            enderecoRepository.delete(endereco);
        }

        return Response.noContent().build();
    }

    private EcopontoDTO convertToDTO(Ecoponto ecoponto) {
        EcopontoDTO dto = new EcopontoDTO();
        dto.setId(ecoponto.getId());
        dto.setDescricao(ecoponto.getDescricao());
        dto.setLatitude(ecoponto.getLatitude());
        dto.setLongitude(ecoponto.getLongitude());
        dto.setCep(ecoponto.getCep());
        dto.setAceitaLixoEletronico(ecoponto.getAceitaLixoEletronico());
        
        // Dados de acesso
        if (ecoponto.getAcesso() != null) {
            Acesso acesso = ecoponto.getAcesso();
            dto.setAcessoId(acesso.getId());
            dto.setLogin(acesso.getLogin());
            // Não incluímos a senha por questões de segurança
        }
        
        // Dados de endereço
        if (ecoponto.getEndereco() != null) {
            Endereco endereco = ecoponto.getEndereco();
            dto.setEnderecoId(endereco.getId());
            dto.setLogradouro(endereco.getLogradouro());
            dto.setBairro(endereco.getBairro());
            dto.setComplemento(endereco.getComplemento());
            dto.setCep(endereco.getCep());
        }
        
        return dto;
    }
} 