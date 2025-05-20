package org.acme.controllers.ecoponto;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
    Template conf_Ecoponto;

    @Inject
    TipoLixoAceitoEcopontoRepository tipoLixoAceitoRepo;

    @Inject
    EcopontoRepository ecopontoRepo;

    @Inject
    TipoLixoRepository tipoLixoRepo;

    @POST
    @Path("/salvar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public TemplateInstance salvarTiposLixo(
            @FormParam("aceito_plastico") String aceitoPlastico,
            @FormParam("pontos_plastico") Double pontosPlastico,
            @FormParam("aceito_vidro") String aceitoVidro,
            @FormParam("pontos_vidro") Double pontosVidro,
            @FormParam("aceito_papel") String aceitoPapel,
            @FormParam("pontos_papel") Double pontosPapel,
            @FormParam("aceito_metal") String aceitoMetal,
            @FormParam("pontos_metal") Double pontosMetal,
            @FormParam("aceito_eletronico") String aceitoEletronico,
            @FormParam("pontos_eletronico") Double pontosEletronico
    ) {
        atualizarOuCriar("Vidro", aceitoVidro, pontosVidro, 1L);
        atualizarOuCriar("Papel", aceitoPapel, pontosPapel, 1L);
        atualizarOuCriar("Metal", aceitoMetal, pontosMetal, 1L);
        atualizarOuCriar("Plastico",aceitoPlastico, pontosPlastico, 1L);
        atualizarOuCriar("Eletronico",aceitoEletronico, pontosEletronico, 1L);

        return conf_Ecoponto.data("success", true);
    }


    private void atualizarOuCriar(String nomeTipo, String aceito, Double pontos, Long idEcoponto) {
        Long idTipo = tipoLixoRepo.findByNome(nomeTipo).getId();
        TipoLixoAceitoEcoponto existente = tipoLixoAceitoRepo.findByEcopontoAndTipoLixo(idEcoponto, idTipo);

        if (aceito != null) {
            if (existente == null) {
                // Criar
                TipoLixoAceitoEcoponto novo = new TipoLixoAceitoEcoponto();
                novo.setEcoponto(ecopontoRepo.findById(idEcoponto));
                novo.setTipoLixo(tipoLixoRepo.findById(idTipo));
                novo.setPontosKg(pontos);
                tipoLixoAceitoRepo.persist(novo);
            } else {
                // Atualizar
                existente.setPontosKg(pontos);
                tipoLixoAceitoRepo.persist(existente);
            }
        } else {
            if (existente != null) {
                // Remover
                tipoLixoAceitoRepo.delete(existente);
            }
        }
    }


    @GET
    @Path("/tipos-lixo-aceitos/{idEcoponto}")
    public Response listarTiposLixoAceitos(@PathParam("idEcoponto") Long idEcoponto) {
        List<TipoLixoAceitoEcoponto> tiposAceitos = tipoLixoAceitoRepo.findByEcopontoId(idEcoponto);
        System.out.println(tiposAceitos);
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
