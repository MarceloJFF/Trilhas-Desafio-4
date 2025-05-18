package org.acme.repositories;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.TipoLixo;

@ApplicationScoped
public class TipoLixoRepository implements PanacheRepository<TipoLixo> {}
