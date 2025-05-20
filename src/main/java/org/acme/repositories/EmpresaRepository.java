package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entities.Empresa;

@ApplicationScoped
public class EmpresaRepository implements PanacheRepository<Empresa> {
} 