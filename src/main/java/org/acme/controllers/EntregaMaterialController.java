package org.acme.controllers;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.models.*;
import org.acme.repositories.*;

import java.math.BigDecimal;
import java.util.List;

@Path("/entregas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
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

    @GET
    public List<EntregaMaterial> listarEntregas() {
        return entregaMaterialRepository.listAll();
    }

    @POST
    @Path("/nova")
    @Transactional
    public Response criarEntrega(
            @FormParam("id_usuario") Long idUsuario,
            @FormParam("id_ecoponto") Long idEcoponto,
            @FormParam("data_entrega") Long dataEntrega,
            @FormParam("qtd_pontos_gerados") Long pontosGerados,
            @FormParam("id_empresa") Long idEmpresa,
            @FormParam("valor_total_gerado") Long valorTotal,
            @FormParam("tipos_lixo") List<Long> tiposLixoIds,
            @FormParam("kgs") List<BigDecimal> kgs
    ) {
        if (tiposLixoIds.size() != kgs.size()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("A quantidade de tipos de lixo e kg deve ser igual").build();
        }

        Usuario usuario = usuarioRepository.findById(idUsuario);
        Ecoponto ecoponto = ecopontoRepository.findById(idEcoponto);
        if (usuario == null || ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário ou Ecoponto não encontrado").build();
        }

        EntregaMaterial entrega = new EntregaMaterial();
        entrega.setUsuario(usuario);
        entrega.setEcoponto(ecoponto);
        entrega.setDataEntrega(dataEntrega);
        entrega.setQtdPontosGerados(pontosGerados);
        entrega.setIdEmpresa(idEmpresa);
        entrega.setValorTotalGerado(valorTotal);

        entregaMaterialRepository.persist(entrega);

        for (int i = 0; i < tiposLixoIds.size(); i++) {
            TipoLixo tipoLixo = tipoLixoRepository.findById(tiposLixoIds.get(i));
            if (tipoLixo == null) continue;

            EntregaTipoLixo entregaTipo = new EntregaTipoLixo();
            entregaTipo.setEntregaMaterial(entrega);
            entregaTipo.setTipoLixo(tipoLixo);
            entregaTipo.setKg(kgs.get(i));
            entregaTipoLixoRepository.persist(entregaTipo);
        }

        return Response.status(Response.Status.CREATED).entity(entrega).build();
    }

    @GET
    @Path("/{id}")
    public EntregaMaterial obter(@PathParam("id") Long id) {
        return entregaMaterialRepository.findById(id);
    }

    @GET
    @Path("/ecoponto/{idEcoponto}")
    public Response listarEntregasPorEcoponto(@PathParam("idEcoponto") Long idEcoponto) {
        List<EntregaMaterial> entregas = entregaMaterialRepository.list("ecoponto.id", idEcoponto);
        if (entregas.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Nenhuma entrega encontrada para o ecoponto informado").build();
        }
        return Response.ok(entregas).build();
    }


    @DELETE
    @Path("/{id}")
    @Transactional
    public void excluir(@PathParam("id") Long id) {
        EntregaMaterial entrega = entregaMaterialRepository.findById(id);
        if (entrega != null) {
            entregaTipoLixoRepository.delete("entregaMaterial.id", id);
            entregaMaterialRepository.delete(entrega);
        }
    }
}

