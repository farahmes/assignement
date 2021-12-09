package ma.octo.assignement.repository;

import ma.octo.assignement.domain.AuditVirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource
public interface AuditVirementRepository extends JpaRepository<AuditVirement, Long> {
}
