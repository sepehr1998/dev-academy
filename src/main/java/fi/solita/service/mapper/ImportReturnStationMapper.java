package fi.solita.service.mapper;

import fi.solita.service.dto.StationDTO;
import fi.solita.web.rest.vm.ImportDataVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImportReturnStationMapper extends EntityMapper<StationDTO, ImportDataVM> {


    @Override
    @Mapping(target = "name",source = "returnStationName")
    @Mapping(target = "id",source = "returnStationId")
    @Mapping(target = "endingIn",expression = "java(0L)")
    @Mapping(target = "startedFrom",expression = "java(0L)")
    StationDTO toDto(ImportDataVM entity);
}
