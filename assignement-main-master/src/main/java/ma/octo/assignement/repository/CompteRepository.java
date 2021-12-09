package ma.octo.assignement.repository;

import ma.octo.assignement.domain.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
@RestResource
public interface CompteRepository extends JpaRepository<Compte, Long> {
  Compte findByNrCompte(String nrCompte);
}
