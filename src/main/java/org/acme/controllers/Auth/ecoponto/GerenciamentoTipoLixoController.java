package org.acme.controllers.Auth.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.TipoLixoAceitoEcopontoDTO;
import org.acme.models.Acesso;
import org.acme.models.Ecoponto;
import org.acme.models.TipoLixoAceitoEcoponto;
import org.acme.repositories.AcessoRepository;
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

    @Inject
    AcessoRepository acessoRepository;

    @POST
    @Path("/salvar")
    @Transactional
    public Response salvarTiposLixo(
            @QueryParam("login") String login,
            List<TipoLixoAceitoEcopontoDTO> tiposLixo
    ) {
        Acesso acesso = acessoRepository.findByLogin(login);
        if (acesso == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado")
                    .build();
        }

        Ecoponto ecoponto = ecopontoRepo.findEcopontoByIdAcesso(acesso.getId());
        if (ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }

        for (TipoLixoAceitoEcopontoDTO tipoLixoDTO : tiposLixo) {
            atualizarOuCriar(tipoLixoDTO, ecoponto.getId());
        }

        return listarTiposLixoAceitos(login);
    }

    private void atualizarOuCriar(TipoLixoAceitoEcopontoDTO dto, Long idEcoponto) {
        TipoLixoAceitoEcoponto existente = tipoLixoAceitoRepo.findByEcopontoAndTipoLixo(idEcoponto, dto.getTipoLixoId());
        System.out.println("DTO recebido: " + dto);
        
        // Se aceito for null, assume como true
        boolean aceito = dto.getAceito() != null ? dto.getAceito() : true;
        
        if (existente == null) {
            // Criar novo
            if(aceito){
                TipoLixoAceitoEcoponto novo = new TipoLixoAceitoEcoponto();
                novo.setEcoponto(ecopontoRepo.findById(idEcoponto));
                novo.setTipoLixo(tipoLixoRepo.findById(dto.getTipoLixoId()));
                novo.setPontosKg(dto.getPontosKg());
                novo.setImg(dto.getImg());
                novo.setAceito(true);
                tipoLixoAceitoRepo.persist(novo);
                System.out.println("Novo tipo de lixo criado: " + novo);
            }
        } else {
            // Atualizar existente
            if(aceito){
                existente.setPontosKg(dto.getPontosKg());
                existente.setImg(dto.getImg());
                existente.setAceito(true);
                tipoLixoAceitoRepo.persist(existente);
                System.out.println("Tipo de lixo atualizado: " + existente);
            } else {
                System.out.println("Deletando tipo de lixo: " + existente);
                tipoLixoAceitoRepo.delete(existente);
            }
        }
    }

    @GET
    @Path("/tipos-lixo-aceitos")
    public Response listarTiposLixoAceitos(@QueryParam("login") String login) {
        Acesso acesso = acessoRepository.findByLogin(login);
        if (acesso == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado")
                    .build();
        }

        Ecoponto ecoponto = ecopontoRepo.findEcopontoByIdAcesso(acesso.getId());
        if (ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }

        List<TipoLixoAceitoEcoponto> tiposAceitos = tipoLixoAceitoRepo.findByEcopontoId(ecoponto.getId());
       
        return Response.ok(tiposAceitos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList())).build();
    }

    private TipoLixoAceitoEcopontoDTO convertToDTO(TipoLixoAceitoEcoponto tipoLixoAceito) {
        System.out.println("Convertendo para DTO: " + tipoLixoAceito);
        TipoLixoAceitoEcopontoDTO dto = new TipoLixoAceitoEcopontoDTO();
        dto.setId(tipoLixoAceito.getId());
        dto.setPontosKg(tipoLixoAceito.getPontosKg());
        dto.setImg(tipoLixoAceito.getImg());
        dto.setEcopontoId(tipoLixoAceito.getEcoponto().getId());
        dto.setTipoLixoId(tipoLixoAceito.getTipoLixo().getId());
        dto.setAceito(tipoLixoAceito.getAceito());
        System.out.println("DTO convertido: " + dto);
        return dto;
    }

    @DELETE
    @Path("/tipo-lixo-aceito/{id}")
    @Transactional
    public Response excluirTipoLixoAceito(
            @QueryParam("login") String login,
            @PathParam("id") Long id
    ) {
        Acesso acesso = acessoRepository.findByLogin(login);
        if (acesso == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado")
                    .build();
        }

        Ecoponto ecoponto = ecopontoRepo.findEcopontoByIdAcesso(acesso.getId());
        if (ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }

        TipoLixoAceitoEcoponto tipoLixoAceito = tipoLixoAceitoRepo.findById(id);
        if (tipoLixoAceito == null || !tipoLixoAceito.getEcoponto().getId().equals(ecoponto.getId())) {
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
