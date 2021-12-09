package ma.octo.assignement.repository;

import ma.octo.assignement.domain.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
@RestResource
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
}
