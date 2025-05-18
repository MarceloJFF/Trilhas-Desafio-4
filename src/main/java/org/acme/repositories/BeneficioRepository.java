package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.Beneficio;

@ApplicationScoped
public class BeneficioRepository implements PanacheRepository<Beneficio> {
}
