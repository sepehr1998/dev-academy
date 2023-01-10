package fi.solita.service.mapper;

import fi.solita.domain.Station;
import fi.solita.service.dto.StationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Station} and its DTO {@link StationDTO}.
 */
@Mapper(componentModel = "spring")
public interface StationMapper extends EntityMapper<StationDTO, Station> {}
