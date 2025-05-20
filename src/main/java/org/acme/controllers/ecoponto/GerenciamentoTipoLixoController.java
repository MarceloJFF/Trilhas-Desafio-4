package org.acme.controllers.ecoponto;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.TipoLixoAceitoEcopontoDTO;
import org.acme.models.TipoLixoAceitoEcoponto;
import org.acme.repositories.EcopontoRepository;
import org.acme.repositories.TipoLixoAceitoEcopontoRepository;
import org.acme.repositories.TipoLixoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Path("/gerenciamento-ecoponto-tipo-lixo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GerenciamentoTipoLixoController {

    @Inject
    TipoLixoAceitoEcopontoRepository tipoLixoAceitoRepo;

    @Inject
    EcopontoRepository ecopontoRepo;

    @Inject
    TipoLixoRepository tipoLixoRepo;

    @POST
    @Path("/{idEcoponto}/salvar")
    @Transactional
    public Response salvarTiposLixo(
            @PathParam("idEcoponto") Long idEcoponto,
            List<TipoLixoAceitoEcopontoDTO> tiposLixo
    ) {
        if (!ecopontoRepo.existsById(idEcoponto)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }

        for (TipoLixoAceitoEcopontoDTO tipoLixoDTO : tiposLixo) {
            atualizarOuCriar(tipoLixoDTO, idEcoponto);
        }

        return listarTiposLixoAceitos(idEcoponto);
    }

    private void atualizarOuCriar(TipoLixoAceitoEcopontoDTO dto, Long idEcoponto) {
        TipoLixoAceitoEcoponto existente = tipoLixoAceitoRepo.findByEcopontoAndTipoLixo(idEcoponto, dto.getTipoLixoId());

        if (existente == null) {
            // Criar novo
            TipoLixoAceitoEcoponto novo = new TipoLixoAceitoEcoponto();
            novo.setEcoponto(ecopontoRepo.findById(idEcoponto));
            novo.setTipoLixo(tipoLixoRepo.findById(dto.getTipoLixoId()));
            novo.setPontosKg(dto.getPontosKg());
            novo.setImg(dto.getImg());
            tipoLixoAceitoRepo.persist(novo);
        } else {
            // Atualizar existente
            existente.setPontosKg(dto.getPontosKg());
            existente.setImg(dto.getImg());
            tipoLixoAceitoRepo.persist(existente);
        }
    }

    @GET
    @Path("/{idEcoponto}/tipos-lixo-aceitos")
    public Response listarTiposLixoAceitos(@PathParam("idEcoponto") Long idEcoponto) {
        if (!ecopontoRepo.existsById(idEcoponto)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }

        List<TipoLixoAceitoEcoponto> tiposAceitos = tipoLixoAceitoRepo.findByEcopontoId(idEcoponto);
        List<TipoLixoAceitoEcopontoDTO> tiposAceitosDTO = tiposAceitos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Response.ok(tiposAceitosDTO).build();
    }

    private TipoLixoAceitoEcopontoDTO convertToDTO(TipoLixoAceitoEcoponto tipoLixoAceito) {
        TipoLixoAceitoEcopontoDTO dto = new TipoLixoAceitoEcopontoDTO();
        dto.setId(tipoLixoAceito.getId());
        dto.setPontosKg(tipoLixoAceito.getPontosKg());
        dto.setImg(tipoLixoAceito.getImg());
        dto.setEcopontoId(tipoLixoAceito.getEcoponto().getId());
        dto.setTipoLixoId(tipoLixoAceito.getTipoLixo().getId());
        return dto;
    }

    @DELETE
    @Path("/{idEcoponto}/tipo-lixo-aceito/{id}")
    public Response excluirTipoLixoAceito(
            @PathParam("idEcoponto") Long idEcoponto,
            @PathParam("id") Long id
    ) {
        if (!ecopontoRepo.existsById(idEcoponto)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }

        TipoLixoAceitoEcoponto tipoLixoAceito = tipoLixoAceitoRepo.findById(id);
        if (tipoLixoAceito == null || !tipoLixoAceito.getEcoponto().getId().equals(idEcoponto)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tipo de lixo aceito não encontrado")
                    .build();
        }

        boolean deleted = tipoLixoAceitoRepo.deleteById(id);
        if (deleted) {
            return Response.ok()
                    .entity("Tipo de lixo aceito excluído com sucesso")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao excluir tipo de lixo aceito")
                    .build();
        }
    }
}
