package fi.solita.service;

import fi.solita.domain.*; // for static metamodels
import fi.solita.domain.Journey;
import fi.solita.repository.JourneyRepository;
import fi.solita.service.criteria.JourneyCriteria;
import fi.solita.service.dto.JourneyDTO;
import fi.solita.service.mapper.JourneyMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Journey} entities in the database.
 * The main input is a {@link JourneyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link JourneyDTO} or a {@link Page} of {@link JourneyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class JourneyQueryService extends QueryService<Journey> {

    private final Logger log = LoggerFactory.getLogger(JourneyQueryService.class);

    private final JourneyRepository journeyRepository;

    private final JourneyMapper journeyMapper;

    public JourneyQueryService(JourneyRepository journeyRepository, JourneyMapper journeyMapper) {
        this.journeyRepository = journeyRepository;
        this.journeyMapper = journeyMapper;
    }

    /**
     * Return a {@link List} of {@link JourneyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<JourneyDTO> findByCriteria(JourneyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Journey> specification = createSpecification(criteria);
        return journeyMapper.toDto(journeyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link JourneyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<JourneyDTO> findByCriteria(JourneyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Journey> specification = createSpecification(criteria);
        return journeyRepository.findAll(specification, page).map(journeyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(JourneyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Journey> specification = createSpecification(criteria);
        return journeyRepository.count(specification);
    }

    /**
     * Function to convert {@link JourneyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Journey> createSpecification(JourneyCriteria criteria) {
        Specification<Journey> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Journey_.id));
            }
            if (criteria.getDistance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDistance(), Journey_.distance));
            }
            if (criteria.getDuration() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuration(), Journey_.duration));
            }
            if (criteria.getDepartureStationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartureStationId(),
                            root -> root.join(Journey_.departureStation, JoinType.LEFT).get(Station_.id)
                        )
                    );
            }
            if (criteria.getReturnStationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReturnStationId(),
                            root -> root.join(Journey_.returnStation, JoinType.LEFT).get(Station_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
