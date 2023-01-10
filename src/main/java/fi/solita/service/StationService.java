package fi.solita.service;

import fi.solita.service.dto.StationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fi.solita.domain.Station}.
 */
public interface StationService {
    /**
     * Save a station.
     *
     * @param stationDTO the entity to save.
     * @return the persisted entity.
     */
    StationDTO save(StationDTO stationDTO);

    /**
     * Updates a station.
     *
     * @param stationDTO the entity to update.
     * @return the persisted entity.
     */
    StationDTO update(StationDTO stationDTO);

    /**
     * Partially updates a station.
     *
     * @param stationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<StationDTO> partialUpdate(StationDTO stationDTO);

    /**
     * Get all the stations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<StationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" station.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StationDTO> findOne(Long id);

    /**
     * Delete the "id" station.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
