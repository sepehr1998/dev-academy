package fi.solita.service.mapper;

import fi.solita.domain.Station;
import fi.solita.domain.StationImport;
import fi.solita.service.dto.StationDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Station} and its DTO {@link StationDTO}.
 */
@Mapper(componentModel = "spring")
public interface StationImportMapper extends EntityMapper<StationDTO, StationImport> {}
