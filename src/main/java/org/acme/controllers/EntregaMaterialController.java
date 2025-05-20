package org.acme.controllers;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.EntregaMaterialDTO;
import org.acme.entities.*;
import org.acme.repositories.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Path("/entregas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EntregaMaterialController {

    @Inject
    EntregaMaterialRepository entregaMaterialRepository;

    @Inject
    EntregaTipoLixoRepository entregaTipoLixoRepository;

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    EcopontoRepository ecopontoRepository;

    @Inject
    TipoLixoRepository tipoLixoRepository;

    @Inject
    TipoLixoAceitoEcopontoRepository tipoLixoAceitoEcopontoRepository;

    @GET
    public Response listarEntregas() {
        List<EntregaMaterial> entregas = entregaMaterialRepository.listAll();
        List<EntregaMaterialDTO> entregasDTO = entregas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Response.ok(entregasDTO).build();
    }

    @POST
    @Path("/nova")
    @Transactional
    public Response criarEntrega(EntregaMaterialDTO dto) {
        if (dto.getTiposLixoIds().size() != dto.getKgs().size()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A quantidade de tipos de lixo e kg deve ser igual")
                    .build();
        }

        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario());
        Ecoponto ecoponto = ecopontoRepository.findById(dto.getIdEcoponto());
        if (usuario == null || ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário ou Ecoponto não encontrado")
                    .build();
        }

        // Buscar as regras de pontuação do ecoponto
        List<TipoLixoAceitoEcoponto> regrasPontuacao = tipoLixoAceitoEcopontoRepository
                .list("ecoponto.id", ecoponto.getId());

        if (regrasPontuacao.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não possui regras de pontuação configuradas")
                    .build();
        }

        // Criar a entrega
        EntregaMaterial entrega = new EntregaMaterial();
        entrega.setUsuario(usuario);
        entrega.setEcoponto(ecoponto);
        entrega.setDataEntrega(dto.getDataEntrega());
        entrega.setIdEmpresa(dto.getIdEmpresa());

        // Calcular pontos e valor total
        long pontosTotais = 0;
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (int i = 0; i < dto.getTiposLixoIds().size(); i++) {
            Long tipoLixoId = dto.getTiposLixoIds().get(i);
            BigDecimal quantidade = dto.getKgs().get(i);

            // Encontrar a regra de pontuação para este tipo de lixo
            TipoLixoAceitoEcoponto regra = regrasPontuacao.stream()
                    .filter(r -> r.getTipoLixo().getId().equals(tipoLixoId))
                    .findFirst()
                    .orElse(null);

            if (regra == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Tipo de lixo " + tipoLixoId + " não é aceito neste ecoponto")
                        .build();
            }

            // Calcular pontos para este tipo de lixo
            long pontos = (long) (quantidade.doubleValue() * regra.getPontosPorKg());
            pontosTotais += pontos;

            // Calcular valor (se houver valor por kg)
            if (regra.getValorPorKg() != null) {
                BigDecimal valor = quantidade.multiply(regra.getValorPorKg());
                valorTotal = valorTotal.add(valor);
            }

            // Criar registro de entrega por tipo de lixo
            TipoLixo tipoLixo = tipoLixoRepository.findById(tipoLixoId);
            if (tipoLixo != null) {
                EntregaTipoLixo entregaTipo = new EntregaTipoLixo();
                entregaTipo.setEntregaMaterial(entrega);
                entregaTipo.setTipoLixo(tipoLixo);
                entregaTipo.setKg(quantidade);
                entregaTipoLixoRepository.persist(entregaTipo);
            }
        }

        // Atualizar pontos do usuário
        usuario.setSaldoPontos(usuario.getSaldoPontos() + pontosTotais);
        usuarioRepository.persist(usuario);

        // Finalizar a entrega
        entrega.setQtdPontosGerados(pontosTotais);
        entrega.setValorTotalGerado(valorTotal.longValue());
        entregaMaterialRepository.persist(entrega);

        return Response.status(Response.Status.CREATED)
                .entity(convertToDTO(entrega))
                .build();
    }

    @GET
    @Path("/{id}")
    public Response obter(@PathParam("id") Long id) {
        EntregaMaterial entrega = entregaMaterialRepository.findById(id);
        if (entrega == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(convertToDTO(entrega)).build();
    }

    @GET
    @Path("/ecoponto/{idEcoponto}")
    public Response listarEntregasPorEcoponto(@PathParam("idEcoponto") Long idEcoponto) {
        List<EntregaMaterial> entregas = entregaMaterialRepository.list("ecoponto.id", idEcoponto);
        if (entregas.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nenhuma entrega encontrada para o ecoponto informado")
                    .build();
        }
        List<EntregaMaterialDTO> entregasDTO = entregas.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Response.ok(entregasDTO).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response excluir(@PathParam("id") Long id) {
        EntregaMaterial entrega = entregaMaterialRepository.findById(id);
        if (entrega == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        entregaTipoLixoRepository.delete("entregaMaterial.id", id);
        entregaMaterialRepository.delete(entrega);
        return Response.noContent().build();
    }

    private EntregaMaterialDTO convertToDTO(EntregaMaterial entrega) {
        EntregaMaterialDTO dto = new EntregaMaterialDTO();
        dto.setId(entrega.getId());
        dto.setIdUsuario(entrega.getUsuario().getId());
        dto.setIdEcoponto(entrega.getEcoponto().getId());
        dto.setDataEntrega(entrega.getDataEntrega());
        dto.setQtdPontosGerados(entrega.getQtdPontosGerados());
        dto.setIdEmpresa(entrega.getIdEmpresa());
        dto.setValorTotalGerado(entrega.getValorTotalGerado());
        
        List<EntregaTipoLixo> tiposLixo = entregaTipoLixoRepository.list("entregaMaterial.id", entrega.getId());
        dto.setTiposLixoIds(tiposLixo.stream()
                .map(etl -> etl.getTipoLixo().getId())
                .collect(Collectors.toList()));
        dto.setKgs(tiposLixo.stream()
                .map(EntregaTipoLixo::getKg)
                .collect(Collectors.toList()));
        
        return dto;
    }
}

