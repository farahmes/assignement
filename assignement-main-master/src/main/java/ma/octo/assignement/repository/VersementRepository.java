package ma.octo.assignement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import ma.octo.assignement.domain.Versement;

@RepositoryRestResource
public interface VersementRepository extends JpaRepository<Versement, Long> {

}
