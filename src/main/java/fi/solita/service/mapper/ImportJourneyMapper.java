package fi.solita.service.mapper;

import fi.solita.service.dto.JourneyDTO;
import fi.solita.web.rest.vm.ImportDataVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;

@Mapper(componentModel = "spring")
public interface ImportJourneyMapper extends EntityMapper<JourneyDTO, ImportDataVM> {


    @Mapping(target = "returnStation", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "departureStation", ignore = true)
    @Override
    @Mapping(target = "distance",source = "distance")
    @Mapping(target = "duration",source = "duration")
    JourneyDTO toDto(ImportDataVM entity);


    default Long map(Duration duration){
        return duration.getSeconds();
    }

    default Duration map(Long duration){
        return Duration.ofSeconds(duration);
    }
}
