package org.acme.controllers.Auth.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.EventoDTO;
import org.acme.models.Endereco;
import org.acme.models.Evento;
import org.acme.models.Ecoponto;
import org.acme.repositories.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.acme.models.Acesso;
import java.time.LocalDate;

@Path("/eventos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventoController {

    @Inject
    EventoRepository eventoRepo;

    @Inject
    EcopontoRepository ecopontoRepo;

    @Inject
    AcessoRepository acessoRepository;

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
        System.out.print(eventos);

        return Response.ok(eventos).build();
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
        Acesso acesso = acessoRepository.findByLogin(eventoDTO.getLoginUsuario());
        Ecoponto ecoponto = ecopontoRepo.findEcopontoByIdAcesso(acesso.getId());
        System.out.print(ecoponto);
        if (ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não encontrado").build();
        }


        Endereco endereco = new Endereco();
        endereco.setComplemento(eventoDTO.getEndereco());
        enderecoRepo.persist(endereco);

        Evento evento = new Evento();
        evento.setDescricao(eventoDTO.getDescricao());
        evento.setCreatedAt(LocalDateTime.now());
        evento.setEndereco(endereco);
        evento.setObs(eventoDTO.getObs());
        evento.setDataEvento(eventoDTO.getDataEvento());
        evento.setHorarioInicioFuncionamento(eventoDTO.getHorarioInicioFuncionamento());
        evento.setHorarioTerminoFuncionamento(eventoDTO.getHorarioTerminoFuncionamento());
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
        Acesso acesso = acessoRepository.findByLogin(eventoDTO.getLoginUsuario());
        Ecoponto ecoponto = ecopontoRepo.findEcopontoByIdAcesso(acesso.getId());

        Evento evento = eventoRepo.findById(id);
        if (evento == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não encontrado").build();
        }

        Endereco endereco =evento.getEndereco();
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
        dto.setObs(evento.getObs());
        dto.setDataEvento(evento.getDataEvento());
        dto.setHorarioInicioFuncionamento(evento.getHorarioInicioFuncionamento());
        dto.setHorarioTerminoFuncionamento(evento.getHorarioTerminoFuncionamento());
        return dto;
    }
}
