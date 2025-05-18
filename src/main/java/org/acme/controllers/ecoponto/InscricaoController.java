package org.acme.controllers.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.models.Evento;
import org.acme.models.Inscricao;
import org.acme.models.Usuario;
import org.acme.repositories.EventoRepository;
import org.acme.repositories.InscricaoRepository;
import org.acme.repositories.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;

@Path("/inscricoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class InscricaoController {

    @Inject
    InscricaoRepository inscricaoRepo;

    @Inject
    UsuarioRepository usuarioRepo;

    @Inject
    EventoRepository eventoRepo;

    // ✅ LISTAR TODAS
    @GET
    public List<Inscricao> listar() {
        return inscricaoRepo.listAll();
    }

    // ✅ BUSCAR POR ID
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Inscricao inscricao = inscricaoRepo.findById(id);
        if (inscricao == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(inscricao).build();
    }

    // ✅ CRIAR INSCRIÇÃO
    @POST
    @Transactional
    public Response criar(
            @FormParam("idUsuario") Long idUsuario,
            @FormParam("idEvento") Long idEvento
    ) {
        Usuario usuario = usuarioRepo.findById(idUsuario);
        Evento evento = eventoRepo.findById(idEvento);

        if (usuario == null || evento == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Usuário ou Evento inválido").build();
        }

        // Verifica se o usuário já está inscrito no mesmo evento
        boolean existe = inscricaoRepo.existsByUsuarioAndEvento(idUsuario, idEvento);
        if (existe) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Usuário já está inscrito neste evento").build();
        }

        Inscricao inscricao = new Inscricao();
        inscricao.setData(LocalDateTime.now());
        inscricao.setUsuario(usuario);
        inscricao.setEvento(evento);

        inscricaoRepo.persist(inscricao);
        return Response.status(Response.Status.CREATED).entity(inscricao).build();
    }

    // ✅ DELETAR INSCRIÇÃO
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
}
