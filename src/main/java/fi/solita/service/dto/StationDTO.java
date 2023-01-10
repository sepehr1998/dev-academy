package fi.solita.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link fi.solita.domain.Station} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StationDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String address;

    private Long startedFrom;

    private Long endingIn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getStartedFrom() {
        return startedFrom;
    }

    public void setStartedFrom(Long startedFrom) {
        this.startedFrom = startedFrom;
    }

    public Long getEndingIn() {
        return endingIn;
    }

    public void setEndingIn(Long endingIn) {
        this.endingIn = endingIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StationDTO)) {
            return false;
        }

        StationDTO stationDTO = (StationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, stationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StationDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", startedFrom=" + getStartedFrom() +
            ", endingIn=" + getEndingIn() +
            "}";
    }
}
