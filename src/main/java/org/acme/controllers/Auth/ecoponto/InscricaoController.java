package org.acme.controllers.Auth.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.InscricaoDTO;
import org.acme.models.Evento;
import org.acme.models.Inscricao;
import org.acme.models.Usuario;
import org.acme.repositories.EventoRepository;
import org.acme.repositories.InscricaoRepository;
import org.acme.repositories.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

@Path("/inscricoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InscricaoController {

    @Inject
    InscricaoRepository inscricaoRepo;

    @Inject
    EventoRepository eventoRepo;

    @Inject
    UsuarioRepository usuarioRepo;

    @GET
    public Response listarTodos() {
        List<Inscricao> inscricoes = inscricaoRepo.listAll();
        List<InscricaoDTO> inscricoesDTO = inscricoes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Response.ok(inscricoesDTO).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Inscricao inscricao = inscricaoRepo.findById(id);
        if (inscricao == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(inscricao)).build();
    }

    @POST
    @Transactional
    public Response criar(InscricaoDTO inscricaoDTO) {
        Evento evento = eventoRepo.findById(inscricaoDTO.getEventoId());
        if (evento == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Evento não encontrado").build();
        }

        Usuario usuario = usuarioRepo.findById(inscricaoDTO.getUsuarioId());
        if (usuario == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Usuário não encontrado").build();
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setEvento(evento);
        inscricao.setUsuario(usuario);

        inscricaoRepo.persist(inscricao);
        return Response.status(Response.Status.CREATED)
                .entity(convertToDTO(inscricao))
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = inscricaoRepo.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    private InscricaoDTO convertToDTO(Inscricao inscricao) {
        InscricaoDTO dto = new InscricaoDTO();
        dto.setId(inscricao.getId());
        dto.setEventoId(inscricao.getEvento().getId());
        dto.setUsuarioId(inscricao.getUsuario().getId());
        return dto;
    }
}
