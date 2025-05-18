package org.acme.controllers.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.models.Beneficio;
import org.acme.models.Ecoponto;
import org.acme.repositories.BeneficioRepository;
import org.acme.repositories.EcopontoRepository;

import java.util.List;

@Path("/beneficios")
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
@Produces(MediaType.APPLICATION_JSON)
public class BeneficioController {

    @Inject
    BeneficioRepository beneficioRepo;

    @Inject
    EcopontoRepository ecopontoRepo;

    // ✅ LISTAR TODOS
    @GET
    public List<Beneficio> listarTodos() {
        return beneficioRepo.listAll();
    }

    // ✅ BUSCAR POR ID
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Beneficio beneficio = beneficioRepo.findById(id);
        if (beneficio == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(beneficio).build();
    }

    // ✅ CRIAR BENEFÍCIO
    @POST
    @Transactional
    public Response criar(
            @FormParam("descricao") Long descricao,
            @FormParam("qtdPontosNecessarios") Long qtdPontosNecessarios,
            @FormParam("empresa") String empresa,
            @FormParam("idEcoponto") Long idEcoponto
    ) {
        Ecoponto ecoponto = ecopontoRepo.findById(idEcoponto);
        if (ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não encontrado").build();
        }

        Beneficio beneficio = new Beneficio();
        beneficio.setDescricao(descricao);
        beneficio.setQtdPontosNecessarios(qtdPontosNecessarios);
        beneficio.setEmpresa(empresa);
        beneficio.setEcoponto(ecoponto);

        beneficioRepo.persist(beneficio);
        return Response.status(Response.Status.CREATED).entity(beneficio).build();
    }

    // ✅ ATUALIZAR
    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(
            @PathParam("id") Long id,
            @FormParam("descricao") Long descricao,
            @FormParam("qtdPontosNecessarios") Long qtdPontosNecessarios,
            @FormParam("empresa") String empresa,
            @FormParam("idEcoponto") Long idEcoponto
    ) {
        Beneficio beneficio = beneficioRepo.findById(id);
        if (beneficio == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Ecoponto ecoponto = ecopontoRepo.findById(idEcoponto);
        if (ecoponto == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ecoponto não encontrado").build();
        }

        beneficio.setDescricao(descricao);
        beneficio.setQtdPontosNecessarios(qtdPontosNecessarios);
        beneficio.setEmpresa(empresa);
        beneficio.setEcoponto(ecoponto);

        return Response.ok(beneficio).build();
    }

    // ✅ EXCLUIR
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletar(@PathParam("id") Long id) {
        boolean deleted = beneficioRepo.deleteById(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
