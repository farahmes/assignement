package ma.octo.assignement.repository;

import ma.octo.assignement.domain.Virement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;
@RestResource
public interface VirementRepository extends JpaRepository<Virement, Long> {
}
