package org.acme.controllers.Auth.ecoponto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.acme.dto.BeneficioDTO;
import org.acme.dto.TrocaBeneficioDTO;
import org.acme.models.*;
import org.acme.repositories.*;

import java.util.List;
import java.util.stream.Collectors;

@Path("/troca-beneficio")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TrocaBeneficioController {

    @Inject
    TrocaBeneficioRepository trocaBeneficioRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    BeneficioRepository beneficioRepository;
    @Inject
    AcessoRepository acessoRepository;
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
    public Response trocarBeneficio(@QueryParam("login") String login, BeneficioDTO body) {
        if (login == null || body == null || body.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Login ou ID do benefício ausente.").build();
        }

        Acesso acesso = acessoRepository.findByLogin(login);
        if (acesso == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado.").build();
        }

        Usuario usuario = usuarioRepository.findByAcessoId(acesso.getId());
        Beneficio beneficio = beneficioRepository.findById(body.getId());

        if (usuario == null || beneficio == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Usuário ou benefício não encontrado.").build();
        }

        if (beneficio.getExpirado()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Benefício expirado.").build();
        }

        if (usuario.getSaldoPontos() < beneficio.getQtdPontosNecessarios()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Pontos insuficientes.").build();
        }

        usuario.setSaldoPontos(usuario.getSaldoPontos() - beneficio.getQtdPontosNecessarios());
        usuarioRepository.persist(usuario);
        Ecoponto ecoponto = ecopontoRepository.findById(beneficio.getEcoponto().getId());
        TrocaBeneficio troca = new TrocaBeneficio();
        troca.setUsuario(usuario);
        troca.setEcoponto(ecoponto);
        troca.setBeneficio(beneficio);
        troca.setQtdPontosConsumidos(beneficio.getQtdPontosNecessarios());
        // Defina o ecoponto aqui se necessário

        trocaBeneficioRepository.persist(troca);

        return Response.ok("Benefício trocado com sucesso!").build();
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
