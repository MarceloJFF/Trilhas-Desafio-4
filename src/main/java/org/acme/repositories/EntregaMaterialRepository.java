package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.EntregaMaterial;

@ApplicationScoped
public class EntregaMaterialRepository implements PanacheRepository<EntregaMaterial> {
}
