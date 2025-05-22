package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.Acesso;
@ApplicationScoped
public class AcessoRepository  implements PanacheRepository<Acesso> {
    public Acesso findByLogin(String login) {
        return find("login", login).firstResult();
    }
    public Acesso findByLoginLike(String loginParcial) {
        // Adiciona as % para buscar por similaridade
        String likeLogin = "%" + loginParcial + "%";
        return find("login LIKE ?1", likeLogin).firstResult();
    }
}
