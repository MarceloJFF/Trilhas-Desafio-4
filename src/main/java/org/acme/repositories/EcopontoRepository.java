package org.acme.repositories;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.Ecoponto;
@ApplicationScoped
public class EcopontoRepository implements PanacheRepository<Ecoponto> {
    public boolean existsById(Long id) {
        return findById(id) != null;
    }

    public Ecoponto findEcopontoByIdAcesso(Long id){
        return find("acesso.id = ?1", id).firstResult();
    }
}
