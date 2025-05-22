package org.acme.controllers.Auth.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.BeneficioDTO;
import org.acme.models.Acesso;
import org.acme.models.Beneficio;
import org.acme.models.Ecoponto;
import org.acme.models.StatusBeneficio;
import org.acme.models.Usuario;
import org.acme.repositories.AcessoRepository;
import org.acme.repositories.BeneficioRepository;
import org.acme.repositories.EcopontoRepository;
import org.acme.repositories.TrocaBeneficioRepository;
import org.acme.repositories.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/beneficios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BeneficioController {

    @Inject
    BeneficioRepository beneficioRepo;

    @Inject
    EcopontoRepository ecopontoRepo;

    @Inject
    AcessoRepository acessoRepository;
    @Inject
    BeneficioRepository beneficioRepository;
    @Inject
    UsuarioRepository usuarioRepository;
    @Inject
    TrocaBeneficioRepository trocaBeneficioRepository;
    @GET
    public Response listarTodos() {
        List<Beneficio> beneficios = beneficioRepo.listAll();
        List<BeneficioDTO> beneficiosDTO = beneficios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Response.ok(beneficiosDTO).build();
    }

    @GET
    @Path("/ativos")
    public Response listarAtivos(@QueryParam("loginUsuario") String loginUsuario) {
        System.out.println("loginUsuario");
        System.out.println(loginUsuario);
        Acesso acesso = acessoRepository.findByLogin(loginUsuario);
        System.out.println("acesso");
        System.out.println(acesso);
        if (acesso == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Acesso não encontrado")
                    .build();
        }
        Usuario usuario = usuarioRepository.findByAcessoId(acesso.getId());
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado")
                    .build();
        }
        List<Beneficio> beneficios = beneficioRepo.find("status", StatusBeneficio.ATIVO.name()).list();
        List<BeneficioDTO> beneficiosDTO = new ArrayList<>();
        for (Beneficio beneficio : beneficios) {
            if (trocaBeneficioRepository.findByBeneficioIdAndUsuarioId(beneficio.getId(), usuario.getId()) == null) {
                beneficiosDTO.add(convertToDTO(beneficio));
            }
        }
        
        return Response.ok(beneficiosDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Beneficio beneficio = beneficioRepo.findById(id);
        if (beneficio == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(beneficio)).build();
    }

    @POST
    @Transactional
    public Response criar(BeneficioDTO beneficioDTO) {
        Acesso acesso = acessoRepository.findByLogin(beneficioDTO.getLoginUsuario());
        Ecoponto ecoponto = ecopontoRepo.findEcopontoByIdAcesso(acesso.getId());
     
        if (ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não encontrado").build();
        }

        if (beneficioDTO.getDataExpiracao() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Data de expiração é obrigatória").build();
        }

        if (beneficioDTO.getDataExpiracao().isBefore(LocalDateTime.now())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Data de expiração não pode ser anterior à data atual").build();
        }

        if (beneficioDTO.getDescricao() == null || beneficioDTO.getDescricao().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Descrição é obrigatória").build();
        }

        if (beneficioDTO.getQtdPontosNecessarios() == null || beneficioDTO.getQtdPontosNecessarios() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Quantidade de pontos necessários deve ser maior que zero").build();
        }

        if (beneficioDTO.getEmpresa() == null || beneficioDTO.getEmpresa().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Empresa é obrigatória").build();
        }

       
        Beneficio beneficio = new Beneficio();
        beneficio.setDescricao(beneficioDTO.getDescricao());
        beneficio.setQtdPontosNecessarios(beneficioDTO.getQtdPontosNecessarios());
        beneficio.setEmpresa(beneficioDTO.getEmpresa());
        beneficio.setDataExpiracao(beneficioDTO.getDataExpiracao());
        beneficio.setExpirado(false);
        beneficio.setEcoponto(ecoponto);
        beneficio.setStatus(StatusBeneficio.ATIVO.name());


        beneficioRepo.persist(beneficio);
        return Response.status(Response.Status.CREATED)
                .entity(convertToDTO(beneficio))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, BeneficioDTO beneficioDTO) {
        Acesso acesso = acessoRepository.findByLogin(beneficioDTO.getLoginUsuario());
        Ecoponto ecopontoAcesso = ecopontoRepo.findEcopontoByIdAcesso(acesso.getId());
     
        Beneficio beneficio = beneficioRepo.findById(id);
        if (beneficio == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (beneficioDTO.getEcopontoId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID do ecoponto é obrigatório").build();
        }

        Ecoponto ecoponto = ecopontoRepo.findById(beneficioDTO.getEcopontoId());
        if (ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não encontrado").build();
        }

        if (beneficioDTO.getDataExpiracao() != null && 
            beneficioDTO.getDataExpiracao().isBefore(LocalDateTime.now())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Data de expiração não pode ser anterior à data atual").build();
        }

        if (beneficioDTO.getDescricao() != null && !beneficioDTO.getDescricao().trim().isEmpty()) {
            beneficio.setDescricao(beneficioDTO.getDescricao());
        }

        if (beneficioDTO.getQtdPontosNecessarios() != null && beneficioDTO.getQtdPontosNecessarios() > 0) {
            beneficio.setQtdPontosNecessarios(beneficioDTO.getQtdPontosNecessarios());
        }

        if (beneficioDTO.getEmpresa() != null && !beneficioDTO.getEmpresa().trim().isEmpty()) {
            beneficio.setEmpresa(beneficioDTO.getEmpresa());
        }

        if (beneficioDTO.getDataExpiracao() != null) {
            beneficio.setDataExpiracao(beneficioDTO.getDataExpiracao());
        }

        beneficio.setEcoponto(ecoponto);

        // Atualiza o status para CANCELADO se o benefício estiver expirado
        if (beneficioDTO.getExpirado()) {
            beneficio.setStatus(StatusBeneficio.CANCELADO.name());
            beneficio.setExpirado(true);
        } else {
            beneficio.setStatus(StatusBeneficio.ATIVO.name());
            beneficio.setExpirado(false);
        }

        return Response.ok(convertToDTO(beneficio)).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = beneficioRepo.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/usuario/{login}")
    public Response listarPorUsuario(@PathParam("login") String login) {
        List<Beneficio> beneficios = beneficioRepo.find("loginUsuario", login).list();
        List<BeneficioDTO> beneficiosDTO = beneficios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Response.ok(beneficiosDTO).build();
    }

    private BeneficioDTO convertToDTO(Beneficio beneficio) {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setId(beneficio.getId());
        dto.setDescricao(beneficio.getDescricao());
        dto.setQtdPontosNecessarios(beneficio.getQtdPontosNecessarios());
        dto.setEmpresa(beneficio.getEmpresa());
        dto.setDataExpiracao(beneficio.getDataExpiracao());
        dto.setExpirado(beneficio.getExpirado());
        dto.setEcopontoId(beneficio.getEcoponto().getId());
        dto.setStatus(beneficio.getStatus());
        dto.setLoginUsuario(beneficio.getLoginUsuario());
        return dto;
    }
}
