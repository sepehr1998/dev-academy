package fi.solita.web.rest.vm;

import com.opencsv.bean.CsvBindByName;


public class ImportDataVM {


    @CsvBindByName(column = "Departure")
    public String departureTime;
    @CsvBindByName(column = "Return")
    public String returnTime;
    @CsvBindByName(column = "Departure station id")
    public Long departureStationId;
    @CsvBindByName(column = "Departure station name")
    public String departureStationName;
    @CsvBindByName(column = "Return station id")
    public Long returnStationId;
    @CsvBindByName(column = "Return station name")
    public String returnStationName;
    @CsvBindByName(column = "Covered distance (m)")
    public Double distance;
    @CsvBindByName(column = "Duration (sec.)")
    public Long duration;


}
