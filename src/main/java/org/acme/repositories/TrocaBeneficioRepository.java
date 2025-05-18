package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.TrocaBeneficio;

@ApplicationScoped
public class TrocaBeneficioRepository implements PanacheRepository<TrocaBeneficio> {
}
