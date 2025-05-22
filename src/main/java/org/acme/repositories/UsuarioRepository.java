package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.Ecoponto;
import org.acme.models.Usuario;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {
    public Usuario findByLogin(String login) {
        return find("login", login).firstResult();
    }

    public Usuario findByAcessoId(Long id) {
        return find("acesso.id", id).firstResult();
    }
}
