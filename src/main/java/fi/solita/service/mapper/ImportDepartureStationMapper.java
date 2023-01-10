package fi.solita.service.mapper;

import fi.solita.service.dto.StationDTO;
import fi.solita.web.rest.vm.ImportDataVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImportDepartureStationMapper extends EntityMapper<StationDTO, ImportDataVM> {

    @Override
    @Mapping(target = "name",source = "departureStationName")
    @Mapping(target = "id",source = "departureStationId")
    @Mapping(target = "endingIn",expression = "java(0L)")
    @Mapping(target = "startedFrom",expression = "java(0L)")
    StationDTO toDto(ImportDataVM entity);
}
