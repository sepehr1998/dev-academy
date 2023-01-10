package fi.solita.service.impl;

import fi.solita.domain.Station;
import fi.solita.domain.StationImport;
import fi.solita.repository.StationImportRepository;
import fi.solita.repository.StationRepository;
import fi.solita.service.StationImportService;
import fi.solita.service.StationService;
import fi.solita.service.dto.StationDTO;
import fi.solita.service.mapper.StationImportMapper;
import fi.solita.service.mapper.StationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Station}.
 */
@Service
@Transactional
public class StationImportServiceImpl implements StationImportService {

    private final Logger log = LoggerFactory.getLogger(StationImportServiceImpl.class);

    private final StationImportRepository stationRepository;

    private final StationImportMapper stationMapper;

    public StationImportServiceImpl(StationImportRepository stationRepository, StationImportMapper stationMapper) {
        this.stationRepository = stationRepository;
        this.stationMapper = stationMapper;
    }

    @Override
    public StationDTO save(StationDTO stationDTO) {
        log.debug("Request to save Station : {}", stationDTO);
        StationImport station = stationMapper.toEntity(stationDTO);
        station = stationRepository.save(station);
        return stationMapper.toDto(station);
    }


    @Override
    public StationDTO update(StationDTO stationDTO) {
        log.debug("Request to update Station : {}", stationDTO);
        StationImport station = stationMapper.toEntity(stationDTO);
        station = stationRepository.save(station);
        return stationMapper.toDto(station);
    }

    @Override
    public Optional<StationDTO> partialUpdate(StationDTO stationDTO) {
        log.debug("Request to partially update Station : {}", stationDTO);

        return stationRepository
            .findById(stationDTO.getId())
            .map(existingStation -> {
                stationMapper.partialUpdate(existingStation, stationDTO);

                return existingStation;
            })
            .map(stationRepository::save)
            .map(stationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stations");
        return stationRepository.findAll(pageable).map(stationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<StationDTO> findOne(Long id) {
        log.debug("Request to get Station : {}", id);
        return stationRepository.findById(id).map(stationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Station : {}", id);
        stationRepository.deleteById(id);
    }
}
