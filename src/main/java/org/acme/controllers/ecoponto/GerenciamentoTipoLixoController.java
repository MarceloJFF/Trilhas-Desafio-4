package org.acme.controllers.ecoponto;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.models.TipoLixoAceitoEcoponto;
import org.acme.repositories.EcopontoRepository;
import org.acme.repositories.TipoLixoAceitoEcopontoRepository;
import org.acme.repositories.TipoLixoRepository;

import java.util.List;

@Path("/gerenciamento-ecoponto-tipo-lixo")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class GerenciamentoTipoLixoController {

    @Inject
    TipoLixoAceitoEcopontoRepository tipoLixoAceitoRepo;

    @Inject
    EcopontoRepository ecopontoRepo;

    @Inject
    TipoLixoRepository tipoLixoRepo;

    @POST
    @Path("/tipo-lixo-aceito")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response cadastrarTipoLixoAceito(
            @FormParam("pontosKg") Double pontosKg,
            @FormParam("img") String img,
            @FormParam("idEcoponto") Long idEcoponto,
            @FormParam("idTipoLixo") Long idTipoLixo) {

        boolean jaExiste = tipoLixoAceitoRepo.existsByEcopontoAndTipoLixo(idEcoponto, idTipoLixo);
        if (jaExiste) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Este tipo de lixo já foi cadastrado para este ecoponto.")
                    .build();
        }

        TipoLixoAceitoEcoponto novo = new TipoLixoAceitoEcoponto();
        novo.setPontosKg(pontosKg);
        novo.setImg(img);
        novo.setEcoponto(ecopontoRepo.findById(idEcoponto));
        novo.setTipoLixo(tipoLixoRepo.findById(idTipoLixo));

        tipoLixoAceitoRepo.persist(novo);

        return Response.status(Response.Status.CREATED).build();
    }

    @GET
    @Path("/tipos-lixo-aceitos/{idEcoponto}")
    public Response listarTiposLixoAceitos(@PathParam("idEcoponto") Long idEcoponto) {
        List<TipoLixoAceitoEcoponto> tiposAceitos = tipoLixoAceitoRepo.findByEcopontoId(idEcoponto);

        return Response.ok(tiposAceitos).build();
    }

    @DELETE
    @Path("/tipo-lixo-aceito/{id}")
    public Response excluirTipoLixoAceito(@PathParam("id") Long id) {
        boolean deleted = tipoLixoAceitoRepo.deleteById(id);
        if (deleted) {
            return Response.ok().entity("Tipo de lixo aceito excluído com sucesso").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Tipo de lixo aceito não encontrado").build();
        }
    }


}
