package org.acme.controllers.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.models.Evento;
import org.acme.models.Ecoponto;
import org.acme.repositories.EcopontoRepository;
import org.acme.repositories.EventoRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Path("/eventos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class EventoController {

    @Inject
    EventoRepository eventoRepo;

    @Inject
    EcopontoRepository ecopontoRepo;

    // ✅ LISTAR TODOS
    @GET
    public List<Evento> listar() {
        return eventoRepo.listAll();
    }

    // ✅ BUSCAR POR ID
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Evento evento = eventoRepo.findById(id);
        if (evento == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(evento).build();
    }

    // ✅ CRIAR
    @POST
    @Transactional
    public Response criar(
            @FormParam("descricao") String descricao,
            @FormParam("createdAt") String createdAtStr,
            @FormParam("endereco") Long endereco,
            @FormParam("obs") String obs,
            @FormParam("dataEvento") String dataEventoStr,
            @FormParam("horarioInicioFuncionamento") String horarioInicioStr,
            @FormParam("horarioTerminoFuncionamento") String horarioTerminoStr,
            @FormParam("beneficiosDisponiveis") Long beneficiosDisponiveis,
            @FormParam("foto") String foto,
            @FormParam("idEcoponto") Long idEcoponto
    ) {
        Evento evento = new Evento();
        evento.setDescricao(descricao);
        evento.setCreatedAt(LocalDateTime.parse(createdAtStr));
        evento.setEndereco(endereco);
        evento.setObs(obs);
        evento.setDataEvento(LocalDateTime.parse(dataEventoStr));
        evento.setHorarioInicioFuncionamento(LocalTime.parse(horarioInicioStr));
        evento.setHorarioTerminoFuncionamento(LocalTime.parse(horarioTerminoStr));
        evento.setBeneficiosDisponiveis(beneficiosDisponiveis);
        evento.setFoto(foto);

        Ecoponto ecoponto = ecopontoRepo.findById(idEcoponto);
        if (ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não encontrado").build();
        }
        evento.setEcoponto(ecoponto);

        eventoRepo.persist(evento);
        return Response.status(Response.Status.CREATED).build();
    }

    // ✅ ATUALIZAR
    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(
            @PathParam("id") Long id,
            @FormParam("descricao") String descricao,
            @FormParam("createdAt") String createdAtStr,
            @FormParam("endereco") Long endereco,
            @FormParam("obs") String obs,
            @FormParam("dataEvento") String dataEventoStr,
            @FormParam("horarioInicioFuncionamento") String horarioInicioStr,
            @FormParam("horarioTerminoFuncionamento") String horarioTerminoStr,
            @FormParam("beneficiosDisponiveis") Long beneficiosDisponiveis,
            @FormParam("foto") String foto,
            @FormParam("idEcoponto") Long idEcoponto
    ) {
        Evento evento = eventoRepo.findById(id);
        if (evento == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        evento.setDescricao(descricao);
        evento.setCreatedAt(LocalDateTime.parse(createdAtStr));
        evento.setEndereco(endereco);
        evento.setObs(obs);
        evento.setDataEvento(LocalDateTime.parse(dataEventoStr));
        evento.setHorarioInicioFuncionamento(LocalTime.parse(horarioInicioStr));
        evento.setHorarioTerminoFuncionamento(LocalTime.parse(horarioTerminoStr));
        evento.setBeneficiosDisponiveis(beneficiosDisponiveis);
        evento.setFoto(foto);

        Ecoponto ecoponto = ecopontoRepo.findById(idEcoponto);
        if (ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não encontrado").build();
        }
        evento.setEcoponto(ecoponto);

        return Response.ok().build();
    }

    // ✅ DELETAR
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = eventoRepo.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
