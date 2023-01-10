package fi.solita.domain;

import java.io.Serializable;
import java.time.Duration;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Journey.
 */
@Entity
@Table(name = "journey")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Journey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "distance", nullable = false)
    private Double distance;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Duration duration;

    @ManyToOne(optional = false)
    @NotNull
    private Station departureStation;

    @ManyToOne(optional = false)
    @NotNull
    private Station returnStation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Journey id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDistance() {
        return this.distance;
    }

    public Journey distance(Double distance) {
        this.setDistance(distance);
        return this;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Journey duration(Duration duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Station getDepartureStation() {
        return this.departureStation;
    }

    public void setDepartureStation(Station station) {
        this.departureStation = station;
    }

    public Journey departureStation(Station station) {
        this.setDepartureStation(station);
        return this;
    }

    public Station getReturnStation() {
        return this.returnStation;
    }

    public void setReturnStation(Station station) {
        this.returnStation = station;
    }

    public Journey returnStation(Station station) {
        this.setReturnStation(station);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Journey)) {
            return false;
        }
        return id != null && id.equals(((Journey) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Journey{" +
            "id=" + getId() +
            ", distance=" + getDistance() +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
