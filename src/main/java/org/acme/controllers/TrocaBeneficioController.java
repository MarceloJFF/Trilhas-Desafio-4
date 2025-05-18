package org.acme.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.models.*;
import org.acme.repositories.*;

@Path("/troca-beneficio")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class TrocaBeneficioController {

    @Inject
    TrocaBeneficioRepository trocaBeneficioRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    BeneficioRepository beneficioRepository;

    @Inject
    EcopontoRepository ecopontoRepository;

    @POST
    @Transactional
    public Response trocarBeneficio(
            @FormParam("idUsuario") Long idUsuario,
            @FormParam("idBeneficio") Long idBeneficio,
            @FormParam("idEcoponto") Long idEcoponto
    ) {
        Usuario usuario = usuarioRepository.findById(idUsuario);
        Beneficio beneficio = beneficioRepository.findById(idBeneficio);
        Ecoponto ecoponto = ecopontoRepository.findById(idEcoponto);

        if (usuario == null || beneficio == null || ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Usuário, benefício ou ecoponto não encontrado.").build();
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

        return Response.ok(troca).build();
    }
}
