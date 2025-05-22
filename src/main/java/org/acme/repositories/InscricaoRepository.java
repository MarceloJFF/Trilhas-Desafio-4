package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.Inscricao;

@ApplicationScoped
public class InscricaoRepository implements PanacheRepository<Inscricao> {

    public boolean existsByUsuarioAndEvento(Long idUsuario, Long idEvento) {
        return count("usuario.id = ?1 AND evento.id = ?2", idUsuario, idEvento) > 0;
    }

    public Long countByEvento(Long idEvento) {
        return count("evento.id", idEvento);
    }
}
