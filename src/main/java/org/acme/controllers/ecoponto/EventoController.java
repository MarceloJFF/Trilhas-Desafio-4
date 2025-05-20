package org.acme.controllers.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.EventoDTO;
import org.acme.models.Endereco;
import org.acme.models.Evento;
import org.acme.models.Ecoponto;
import org.acme.repositories.EventoRepository;
import org.acme.repositories.EcopontoRepository;
import org.acme.repositories.EnderecoRepository;
import org.acme.repositories.InscricaoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Path("/eventos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventoController {

    @Inject
    EventoRepository eventoRepo;

    @Inject
    EcopontoRepository ecopontoRepo;

    @Inject
    EnderecoRepository enderecoRepo;

    @Inject
    InscricaoRepository inscricaoRepo;

    @GET
    public Response listarTodos() {
        List<Evento> eventos = eventoRepo.listAll();
        List<EventoDTO> eventosDTO = eventos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Response.ok(eventosDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Evento evento = eventoRepo.findById(id);
        if (evento == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(evento)).build();
    }

    @GET
    @Path("/{id}/inscritos")
    public Response contarInscritos(@PathParam("id") Long id) {
        Evento evento = eventoRepo.findById(id);
        if (evento == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Long quantidadeInscritos = inscricaoRepo.countByEvento(id);
        return Response.ok(quantidadeInscritos).build();
    }

    @POST
    @Transactional
    public Response criar(EventoDTO eventoDTO) {
        Ecoponto ecoponto = ecopontoRepo.findById(eventoDTO.getEcopontoId());
        if (ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não encontrado").build();
        }

        Endereco endereco = enderecoRepo.findById(eventoDTO.getEnderecoId());
        if (endereco == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Endereço não encontrado").build();
        }

        Evento evento = new Evento();
        evento.setDescricao(eventoDTO.getDescricao());
        evento.setCreatedAt(LocalDateTime.now());
        evento.setEndereco(endereco);
        evento.setObs(eventoDTO.getObs());
        evento.setDataEvento(eventoDTO.getDataEvento());
        evento.setHorarioInicioFuncionamento(eventoDTO.getHorarioInicioFuncionamento());
        evento.setHorarioTerminoFuncionamento(eventoDTO.getHorarioTerminoFuncionamento());
        evento.setBeneficiosDisponiveis(eventoDTO.getBeneficiosDisponiveis());
        evento.setFoto(eventoDTO.getFoto());
        evento.setEcoponto(ecoponto);

        eventoRepo.persist(evento);
        return Response.status(Response.Status.CREATED)
                .entity(convertToDTO(evento))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, EventoDTO eventoDTO) {
        Evento evento = eventoRepo.findById(id);
        if (evento == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Ecoponto ecoponto = ecopontoRepo.findById(eventoDTO.getEcopontoId());
        if (ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não encontrado").build();
        }

        Endereco endereco = enderecoRepo.findById(eventoDTO.getEnderecoId());
        if (endereco == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Endereço não encontrado").build();
        }

        evento.setDescricao(eventoDTO.getDescricao());
        evento.setEndereco(endereco);
        evento.setObs(eventoDTO.getObs());
        evento.setDataEvento(eventoDTO.getDataEvento());
        evento.setHorarioInicioFuncionamento(eventoDTO.getHorarioInicioFuncionamento());
        evento.setHorarioTerminoFuncionamento(eventoDTO.getHorarioTerminoFuncionamento());
        evento.setBeneficiosDisponiveis(eventoDTO.getBeneficiosDisponiveis());
        evento.setFoto(eventoDTO.getFoto());
        evento.setEcoponto(ecoponto);

        return Response.ok(convertToDTO(evento)).build();
    }

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

    private EventoDTO convertToDTO(Evento evento) {
        EventoDTO dto = new EventoDTO();
        dto.setId(evento.getId());
        dto.setDescricao(evento.getDescricao());
        dto.setCreatedAt(evento.getCreatedAt());
        dto.setEnderecoId(evento.getEndereco().getId());
        dto.setObs(evento.getObs());
        dto.setDataEvento(evento.getDataEvento());
        dto.setHorarioInicioFuncionamento(evento.getHorarioInicioFuncionamento());
        dto.setHorarioTerminoFuncionamento(evento.getHorarioTerminoFuncionamento());
        dto.setBeneficiosDisponiveis(evento.getBeneficiosDisponiveis());
        dto.setFoto(evento.getFoto());
        dto.setEcopontoId(evento.getEcoponto().getId());
        return dto;
    }
}
