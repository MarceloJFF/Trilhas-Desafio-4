package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entities.Login;

@ApplicationScoped
public class LoginRepository implements PanacheRepository<Login> {
    public Login findByLogin(String login) {
        return find("login", login).firstResult();
    }
} 