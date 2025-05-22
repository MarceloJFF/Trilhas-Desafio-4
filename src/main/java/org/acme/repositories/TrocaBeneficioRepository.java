package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.TrocaBeneficio;

@ApplicationScoped
public class TrocaBeneficioRepository implements PanacheRepository<TrocaBeneficio> {

    public TrocaBeneficio findByBeneficioIdAndUsuarioId(Long idBeneficio, Long idUsuario) {
        return find("beneficio.id = ?1 and usuario.id = ?2", idBeneficio, idUsuario).firstResult();
    }
}
