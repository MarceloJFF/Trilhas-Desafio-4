package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.TipoLixoAceitoEcoponto;

import java.util.List;

@ApplicationScoped
public class TipoLixoAceitoEcopontoRepository implements PanacheRepository<TipoLixoAceitoEcoponto> {

    public List<TipoLixoAceitoEcoponto> findByEcopontoId(Long idEcoponto) {
        return find("ecoponto.id", idEcoponto).list();
    }

    public TipoLixoAceitoEcoponto findByEcopontoAndTipoLixo(Long idEcoponto, Long idTipoLixo) {
        return find("ecoponto.id = ?1 and tipoLixo.id = ?2", idEcoponto, idTipoLixo).firstResult();
    }

    public boolean existsByEcopontoAndTipoLixo(Long idEcoponto, Long idTipoLixo) {
        return count("ecoponto.id = ?1 and tipoLixo.id = ?2", idEcoponto, idTipoLixo) > 0;
    }

}
