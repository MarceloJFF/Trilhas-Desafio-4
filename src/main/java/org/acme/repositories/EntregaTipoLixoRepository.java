package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.EntregaTipoLixo;

@ApplicationScoped
public class EntregaTipoLixoRepository implements PanacheRepository<EntregaTipoLixo> {
    // Aqui você pode adicionar métodos personalizados se precisar
}
