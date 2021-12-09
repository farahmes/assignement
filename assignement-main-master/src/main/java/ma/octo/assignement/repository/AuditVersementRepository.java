package ma.octo.assignement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ma.octo.assignement.domain.AuditVersement;

public interface AuditVersementRepository extends JpaRepository<AuditVersement, Long> {

}
