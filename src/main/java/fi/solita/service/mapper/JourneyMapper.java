package fi.solita.service.mapper;

import fi.solita.domain.Journey;
import fi.solita.domain.Station;
import fi.solita.service.dto.JourneyDTO;
import fi.solita.service.dto.StationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Journey} and its DTO {@link JourneyDTO}.
 */
@Mapper(componentModel = "spring")
public interface JourneyMapper extends EntityMapper<JourneyDTO, Journey> {
    @Mapping(target = "departureStation", source = "departureStation", qualifiedByName = "stationName")
    @Mapping(target = "returnStation", source = "returnStation", qualifiedByName = "stationName")
    JourneyDTO toDto(Journey s);

    @Named("stationName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StationDTO toDtoStationName(Station station);
}
