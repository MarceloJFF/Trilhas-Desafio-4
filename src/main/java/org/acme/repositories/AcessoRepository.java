package org.acme.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.models.Acesso;
import org.acme.models.Endereco;
@ApplicationScoped
public class AcessoRepository  implements PanacheRepository<Acesso> {
}
