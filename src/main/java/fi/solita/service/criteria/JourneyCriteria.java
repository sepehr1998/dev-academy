package fi.solita.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fi.solita.domain.Journey} entity. This class is used
 * in {@link fi.solita.web.rest.JourneyResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /journeys?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class JourneyCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter distance;

    private DurationFilter duration;

    private LongFilter departureStationId;

    private LongFilter returnStationId;

    private Boolean distinct;

    public JourneyCriteria() {}

    public JourneyCriteria(JourneyCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.distance = other.distance == null ? null : other.distance.copy();
        this.duration = other.duration == null ? null : other.duration.copy();
        this.departureStationId = other.departureStationId == null ? null : other.departureStationId.copy();
        this.returnStationId = other.returnStationId == null ? null : other.returnStationId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public JourneyCriteria copy() {
        return new JourneyCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getDistance() {
        return distance;
    }

    public DoubleFilter distance() {
        if (distance == null) {
            distance = new DoubleFilter();
        }
        return distance;
    }

    public void setDistance(DoubleFilter distance) {
        this.distance = distance;
    }

    public DurationFilter getDuration() {
        return duration;
    }

    public DurationFilter duration() {
        if (duration == null) {
            duration = new DurationFilter();
        }
        return duration;
    }

    public void setDuration(DurationFilter duration) {
        this.duration = duration;
    }

    public LongFilter getDepartureStationId() {
        return departureStationId;
    }

    public LongFilter departureStationId() {
        if (departureStationId == null) {
            departureStationId = new LongFilter();
        }
        return departureStationId;
    }

    public void setDepartureStationId(LongFilter departureStationId) {
        this.departureStationId = departureStationId;
    }

    public LongFilter getReturnStationId() {
        return returnStationId;
    }

    public LongFilter returnStationId() {
        if (returnStationId == null) {
            returnStationId = new LongFilter();
        }
        return returnStationId;
    }

    public void setReturnStationId(LongFilter returnStationId) {
        this.returnStationId = returnStationId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final JourneyCriteria that = (JourneyCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(distance, that.distance) &&
            Objects.equals(duration, that.duration) &&
            Objects.equals(departureStationId, that.departureStationId) &&
            Objects.equals(returnStationId, that.returnStationId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distance, duration, departureStationId, returnStationId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JourneyCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (distance != null ? "distance=" + distance + ", " : "") +
            (duration != null ? "duration=" + duration + ", " : "") +
            (departureStationId != null ? "departureStationId=" + departureStationId + ", " : "") +
            (returnStationId != null ? "returnStationId=" + returnStationId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
