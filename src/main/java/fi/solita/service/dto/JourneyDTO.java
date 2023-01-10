package fi.solita.service.dto;

import java.io.Serializable;
import java.time.Duration;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fi.solita.domain.Journey} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JourneyDTO implements Serializable {

    private Long id;

    @NotNull
    private Double distance;

    @NotNull
    private Duration duration;

    private StationDTO departureStation;

    private StationDTO returnStation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public StationDTO getDepartureStation() {
        return departureStation;
    }

    public void setDepartureStation(StationDTO departureStation) {
        this.departureStation = departureStation;
    }

    public StationDTO getReturnStation() {
        return returnStation;
    }

    public void setReturnStation(StationDTO returnStation) {
        this.returnStation = returnStation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JourneyDTO)) {
            return false;
        }

        JourneyDTO journeyDTO = (JourneyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, journeyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JourneyDTO{" +
            "id=" + getId() +
            ", distance=" + getDistance() +
            ", duration='" + getDuration() + "'" +
            ", departureStation=" + getDepartureStation() +
            ", returnStation=" + getReturnStation() +
            "}";
    }
}
