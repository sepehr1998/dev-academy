package fi.solita.web.rest;


import com.opencsv.bean.CsvToBeanBuilder;
import fi.solita.service.JourneyService;
import fi.solita.service.StationService;
import fi.solita.service.dto.JourneyDTO;
import fi.solita.service.dto.StationDTO;
import fi.solita.service.impl.StationImportServiceImpl;
import fi.solita.service.mapper.ImportDepartureStationMapper;
import fi.solita.service.mapper.ImportJourneyMapper;
import fi.solita.service.mapper.ImportReturnStationMapper;
import fi.solita.web.rest.vm.ImportDataVM;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ImportController {

    private final StationImportServiceImpl stationService;

    private final JourneyService journeyService;
    private final ImportJourneyMapper importJourneyMapper;
    private final ImportReturnStationMapper importReturnStationMapper;
    private final ImportDepartureStationMapper importDepartureStationMapper;

    public ImportController(StationImportServiceImpl stationService
        , JourneyService journeyService
        , ImportJourneyMapper importJourneyMapper
        , ImportReturnStationMapper importReturnStationMapper
        , ImportDepartureStationMapper importDepartureStationMapper) {
        this.stationService = stationService;
        this.journeyService = journeyService;
        this.importJourneyMapper = importJourneyMapper;
        this.importReturnStationMapper = importReturnStationMapper;
        this.importDepartureStationMapper = importDepartureStationMapper;
    }


    @RequestMapping(value = "/import/csv",method = RequestMethod.POST)
    public ResponseEntity<Map> importFromCsv(@RequestParam("files") MultipartFile file){
        if (!Objects.equals(StringUtils.getFilenameExtension(file.getOriginalFilename()), "csv")){
            throw new UnknownFormatFlagsException("Unsupported file format "
                + StringUtils.getFilenameExtension(file.getOriginalFilename()));
        }
        try {
            InputStreamReader reader = new InputStreamReader(file.getInputStream());
            new CsvToBeanBuilder<ImportDataVM>(reader)
                .withType(ImportDataVM.class)
                .build()
                .parse().stream()
                .filter(importDataVM -> importDataVM.duration >= 10)
                .filter(importDataVM -> importDataVM.distance >= 10)
                .map(importDataVM -> {
                    StationDTO returnDto = stationService.findOne(importDataVM.returnStationId)
                        .orElse(importReturnStationMapper.toDto(importDataVM));
                    StationDTO departureDto = stationService.findOne(importDataVM.departureStationId)
                        .orElse(importDepartureStationMapper.toDto(importDataVM));
                    JourneyDTO journeyDto = importJourneyMapper.toDto(importDataVM);
                    journeyDto.setDepartureStation(departureDto);
                    journeyDto.setReturnStation(returnDto);
                    return journeyDto;
                })
                .peek(journeyDTO -> {
                    journeyDTO.getDepartureStation().setStartedFrom(journeyDTO.getDepartureStation().getStartedFrom()+1);
                    stationService.save(journeyDTO.getDepartureStation());
                })
                .peek(journeyDTO -> {
                    journeyDTO.getReturnStation().setEndingIn(journeyDTO.getReturnStation().getEndingIn()+1);
                    stationService.save(journeyDTO.getReturnStation());
                })
                .forEach(journeyService::save);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(new HashMap<>());
    }

}
