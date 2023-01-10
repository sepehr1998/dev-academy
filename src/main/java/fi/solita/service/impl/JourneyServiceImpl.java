package fi.solita.service.impl;

import fi.solita.domain.Journey;
import fi.solita.repository.JourneyRepository;
import fi.solita.service.JourneyService;
import fi.solita.service.dto.JourneyDTO;
import fi.solita.service.mapper.JourneyMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Journey}.
 */
@Service
@Transactional
public class JourneyServiceImpl implements JourneyService {

    private final Logger log = LoggerFactory.getLogger(JourneyServiceImpl.class);

    private final JourneyRepository journeyRepository;

    private final JourneyMapper journeyMapper;

    public JourneyServiceImpl(JourneyRepository journeyRepository, JourneyMapper journeyMapper) {
        this.journeyRepository = journeyRepository;
        this.journeyMapper = journeyMapper;
    }

    @Override
    public JourneyDTO save(JourneyDTO journeyDTO) {
        log.debug("Request to save Journey : {}", journeyDTO);
        Journey journey = journeyMapper.toEntity(journeyDTO);
        journey = journeyRepository.save(journey);
        return journeyMapper.toDto(journey);
    }

    @Override
    public JourneyDTO update(JourneyDTO journeyDTO) {
        log.debug("Request to update Journey : {}", journeyDTO);
        Journey journey = journeyMapper.toEntity(journeyDTO);
        journey = journeyRepository.save(journey);
        return journeyMapper.toDto(journey);
    }

    @Override
    public Optional<JourneyDTO> partialUpdate(JourneyDTO journeyDTO) {
        log.debug("Request to partially update Journey : {}", journeyDTO);

        return journeyRepository
            .findById(journeyDTO.getId())
            .map(existingJourney -> {
                journeyMapper.partialUpdate(existingJourney, journeyDTO);

                return existingJourney;
            })
            .map(journeyRepository::save)
            .map(journeyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JourneyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Journeys");
        return journeyRepository.findAll(pageable).map(journeyMapper::toDto);
    }

    public Page<JourneyDTO> findAllWithEagerRelationships(Pageable pageable) {
        return journeyRepository.findAllWithEagerRelationships(pageable).map(journeyMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<JourneyDTO> findOne(Long id) {
        log.debug("Request to get Journey : {}", id);
        return journeyRepository.findOneWithEagerRelationships(id).map(journeyMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Journey : {}", id);
        journeyRepository.deleteById(id);
    }
}
