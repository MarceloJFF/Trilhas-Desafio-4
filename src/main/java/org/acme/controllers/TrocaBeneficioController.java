package org.acme.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.TrocaBeneficioDTO;
import org.acme.models.*;
import org.acme.repositories.*;

import java.util.List;
import java.util.stream.Collectors;

@Path("/troca-beneficio")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrocaBeneficioController {

    @Inject
    TrocaBeneficioRepository trocaBeneficioRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    BeneficioRepository beneficioRepository;

    @Inject
    EcopontoRepository ecopontoRepository;

    @GET
    @Path("/usuario/{idUsuario}")
    public Response listarTrocasPorUsuario(@PathParam("idUsuario") Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado")
                    .build();
        }

        List<TrocaBeneficio> trocas = trocaBeneficioRepository.list("usuario.id", idUsuario);
        List<TrocaBeneficioDTO> trocasDTO = trocas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Response.ok(trocasDTO).build();
    }

    @GET
    @Path("/ecoponto/{idEcoponto}")
    public Response listarTrocasPorEcoponto(@PathParam("idEcoponto") Long idEcoponto) {
        Ecoponto ecoponto = ecopontoRepository.findById(idEcoponto);
        if (ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }

        List<TrocaBeneficio> trocas = trocaBeneficioRepository.list("ecoponto.id", idEcoponto);
        List<TrocaBeneficioDTO> trocasDTO = trocas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Response.ok(trocasDTO).build();
    }

    @POST
    @Transactional
    public Response trocarBeneficio(TrocaBeneficioDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario());
        Beneficio beneficio = beneficioRepository.findById(dto.getIdBeneficio());
        Ecoponto ecoponto = ecopontoRepository.findById(dto.getIdEcoponto());

        if (usuario == null || beneficio == null || ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Usuário, benefício ou ecoponto não encontrado.").build();
        }

        if (beneficio.getExpirado()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Este benefício está expirado e não pode mais ser trocado.").build();
        }

        if (usuario.getSaldoPontos() < beneficio.getQtdPontosNecessarios()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Pontos insuficientes para trocar pelo benefício.").build();
        }

        // Subtrai os pontos do usuário
        usuario.setSaldoPontos(usuario.getSaldoPontos() - beneficio.getQtdPontosNecessarios());

        TrocaBeneficio troca = new TrocaBeneficio();
        troca.setUsuario(usuario);
        troca.setBeneficio(beneficio);
        troca.setEcoponto(ecoponto);
        troca.setQtdPontosConsumidos(beneficio.getQtdPontosNecessarios());

        trocaBeneficioRepository.persist(troca);

        return Response.ok(convertToDTO(troca)).build();
    }

    private TrocaBeneficioDTO convertToDTO(TrocaBeneficio troca) {
        TrocaBeneficioDTO dto = new TrocaBeneficioDTO();
        dto.setId(troca.getId());
        
        // Informações do usuário
        dto.setIdUsuario(troca.getUsuario().getId());
        dto.setNomeUsuario(troca.getUsuario().getPrimeiroNome() + " " + troca.getUsuario().getUltimoNome());
        
        // Informações do benefício
        dto.setIdBeneficio(troca.getBeneficio().getId());
        dto.setDescricaoBeneficio(troca.getBeneficio().getDescricao());
        
        // Informações do ecoponto
        dto.setIdEcoponto(troca.getEcoponto().getId());
        dto.setNomeEcoponto(troca.getEcoponto().getDescricao());
        
        // Outras informações
        dto.setQtdPontosConsumidos(troca.getQtdPontosConsumidos());
        dto.setDataTroca(troca.getDataTroca());
        
        return dto;
    }
}
