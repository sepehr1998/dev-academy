package fi.solita.service;

import fi.solita.service.dto.JourneyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fi.solita.domain.Journey}.
 */
public interface JourneyService {
    /**
     * Save a journey.
     *
     * @param journeyDTO the entity to save.
     * @return the persisted entity.
     */
    JourneyDTO save(JourneyDTO journeyDTO);

    /**
     * Updates a journey.
     *
     * @param journeyDTO the entity to update.
     * @return the persisted entity.
     */
    JourneyDTO update(JourneyDTO journeyDTO);

    /**
     * Partially updates a journey.
     *
     * @param journeyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<JourneyDTO> partialUpdate(JourneyDTO journeyDTO);

    /**
     * Get all the journeys.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JourneyDTO> findAll(Pageable pageable);

    /**
     * Get all the journeys with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<JourneyDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" journey.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<JourneyDTO> findOne(Long id);

    /**
     * Delete the "id" journey.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
