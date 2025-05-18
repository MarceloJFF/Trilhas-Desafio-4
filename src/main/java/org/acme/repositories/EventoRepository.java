package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.Evento;

@ApplicationScoped
public class EventoRepository implements PanacheRepository<Evento> {
}