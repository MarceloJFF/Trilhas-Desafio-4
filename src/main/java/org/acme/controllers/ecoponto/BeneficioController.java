package org.acme.controllers.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.BeneficioDTO;
import org.acme.models.Beneficio;
import org.acme.models.Ecoponto;
import org.acme.repositories.BeneficioRepository;
import org.acme.repositories.EcopontoRepository;

import java.time.LocalDateTime;
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
    public Response listarAtivos() {
        List<Beneficio> beneficios = beneficioRepo.find("expirado = false").list();
        List<BeneficioDTO> beneficiosDTO = beneficios.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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
        if (beneficioDTO.getEcopontoId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("ID do ecoponto é obrigatório").build();
        }

        Ecoponto ecoponto = ecopontoRepo.findById(beneficioDTO.getEcopontoId());
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

        beneficioRepo.persist(beneficio);
        return Response.status(Response.Status.CREATED)
                .entity(convertToDTO(beneficio))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, BeneficioDTO beneficioDTO) {
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

    private BeneficioDTO convertToDTO(Beneficio beneficio) {
        BeneficioDTO dto = new BeneficioDTO();
        dto.setId(beneficio.getId());
        dto.setDescricao(beneficio.getDescricao());
        dto.setQtdPontosNecessarios(beneficio.getQtdPontosNecessarios());
        dto.setEmpresa(beneficio.getEmpresa());
        dto.setDataExpiracao(beneficio.getDataExpiracao());
        dto.setExpirado(beneficio.getExpirado());
        dto.setEcopontoId(beneficio.getEcoponto().getId());
        return dto;
    }
}
