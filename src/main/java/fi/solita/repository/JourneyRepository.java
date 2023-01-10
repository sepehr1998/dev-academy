package fi.solita.repository;

import fi.solita.domain.Journey;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Journey entity.
 */
@Repository
public interface JourneyRepository extends JpaRepository<Journey, Long>, JpaSpecificationExecutor<Journey> {
    default Optional<Journey> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Journey> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Journey> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct journey from Journey journey left join fetch journey.departureStation left join fetch journey.returnStation",
        countQuery = "select count(distinct journey) from Journey journey"
    )
    Page<Journey> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct journey from Journey journey left join fetch journey.departureStation left join fetch journey.returnStation")
    List<Journey> findAllWithToOneRelationships();

    @Query(
        "select journey from Journey journey left join fetch journey.departureStation left join fetch journey.returnStation where journey.id =:id"
    )
    Optional<Journey> findOneWithToOneRelationships(@Param("id") Long id);
}
