package org.acme.controllers.Auth.ecoponto;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.dto.EntregaMaterialDTO;
import org.acme.models.*;
import org.acme.repositories.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
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
    AcessoRepository acessoRepository;

    @Inject
    TipoLixoRepository tipoLixoRepository;

    @Inject
    TipoLixoAceitoEcopontoRepository tipoLixoAceitoEcopontoRepository;

 

    @POST
    @Path("/nova")
    @Transactional
    public Response criarEntrega(@QueryParam("loginUsuario") String loginUsuario, EntregaMaterialDTO dto) {
        Acesso acesso = acessoRepository.findByLogin(loginUsuario);
        
        if (acesso == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Acesso não encontrado")
                    .build();
        }
        Usuario usuario = usuarioRepository.findByAcessoId(acesso.getId());
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado")
                    .build();
        }

       

        Ecoponto ecoponto = ecopontoRepository.findById(dto.getIdEcoponto());
        if (usuario == null || ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário ou Ecoponto não encontrado")
                    .build();
        }


        // Criar a entrega
        EntregaMaterial entrega = new EntregaMaterial();
        entrega.setUsuario(usuario);
        entrega.setEcoponto(ecoponto);
        entrega.setDataEntrega(LocalDateTime.now());
        entrega.setIdEmpresa(dto.getIdEmpresa());
        entrega.setDeferido(false);

        // Calcular pontos e valor total
        Random random = new Random();
        long pontosTotais = 1 + random.nextInt(5); // Gera de 1 a 5
        BigDecimal valorTotal = BigDecimal.ZERO;

        entrega.setQtdPontosGerados(pontosTotais + pontosTotais*dto.getKg().longValue());
        entrega.setValorTotalGerado(pontosTotais + pontosTotais*dto.getKg().longValue());
        entregaMaterialRepository.persist(entrega);

        return Response.status(Response.Status.CREATED)
                .entity(convertToDTO(entrega))
                .build();
    }
    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizarStatusEntrega(
            @PathParam("id") Long id,
            @QueryParam("deferido") boolean deferido
    ) {
        EntregaMaterial entrega = entregaMaterialRepository.findById(id);

        if (entrega == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Entrega não encontrada")
                    .build();
        }
        Usuario usuario = usuarioRepository.findById(entrega.getUsuario().getId());
        usuario.setSaldoPontos(usuario.getSaldoPontos()+entrega.getQtdPontosGerados());
        entrega.setDeferido(deferido);
        usuarioRepository.persist(usuario);
        entregaMaterialRepository.persist(entrega);
        return Response.ok(convertToDTO(entrega)).build();
    }

    @GET
    public Response listarEntregasPorEcoponto(@QueryParam("loginEcoponto") String loginEcoponto) {

        Acesso acessoModel = acessoRepository.findByLoginLike(loginEcoponto);
        if (acessoModel == null) {
            System.out.println("Acesso não encontrado para login: " + loginEcoponto);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Acesso não encontrado para login: " + loginEcoponto)
                    .build();
        }
        
        Ecoponto ecoponto = ecopontoRepository.findEcopontoByIdAcesso(acessoModel.getId());
        if (ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado para o acesso fornecido.")
                    .build();
        }
        
        Usuario usuario = usuarioRepository.findByAcessoId(acessoModel.getId());
        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado")
                    .build();
        }

        List<EntregaMaterial> entregas = entregaMaterialRepository.list(
            "usuario.id = ?1 ", 
            usuario.getId()
        );
       
        return Response.ok(entregas).build();
    }

    @GET
    @Path("/ecoponto")
    public Response listarEntregasDoEcoponto(@QueryParam("loginUsuario") String loginUsuario) {
        Acesso acesso = acessoRepository.find("from Acesso where login=?1",loginUsuario).firstResult();
        Ecoponto ecoponto = ecopontoRepository.findEcopontoByIdAcesso(acesso.getId());
        if (ecoponto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Ecoponto não encontrado")
                    .build();
        }
        List<EntregaMaterial> entregas = entregaMaterialRepository.list("ecoponto.id", ecoponto.getId());
        return Response.ok(entregas).build();
    }
    
    @GET
    @Path("/usuario")
    public Response listarEntregasDoUsuario(@QueryParam("loginUsuario") String loginUsuario) {
        Acesso acesso = acessoRepository.find("from Acesso where login=?1",loginUsuario).firstResult();
        Usuario usuario = usuarioRepository.findByAcessoId(acesso.getId());

        if (usuario == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Usuario não encontrado")
                    .build();
        }
        List<EntregaMaterial> entregas = entregaMaterialRepository.list("usuario.id", usuario.getId());
        return Response.ok(entregas).build();
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
        dto.setQtdPontosGerados(entrega.getQtdPontosGerados());
        dto.setIdEmpresa(entrega.getIdEmpresa());
        dto.setValorTotalGerado(entrega.getValorTotalGerado());
        
        return dto;
    }
}

