package fi.solita.repository;

import fi.solita.domain.Station;
import fi.solita.domain.StationImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Station entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StationImportRepository extends JpaRepository<StationImport, Long>, JpaSpecificationExecutor<StationImport> {}
