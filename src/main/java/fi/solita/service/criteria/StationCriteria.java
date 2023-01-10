package fi.solita.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fi.solita.domain.Station} entity. This class is used
 * in {@link fi.solita.web.rest.StationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /stations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter address;

    private LongFilter startedFrom;

    private LongFilter endingIn;

    private Boolean distinct;

    public StationCriteria() {}

    public StationCriteria(StationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.startedFrom = other.startedFrom == null ? null : other.startedFrom.copy();
        this.endingIn = other.endingIn == null ? null : other.endingIn.copy();
        this.distinct = other.distinct;
    }

    @Override
    public StationCriteria copy() {
        return new StationCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public LongFilter getStartedFrom() {
        return startedFrom;
    }

    public LongFilter startedFrom() {
        if (startedFrom == null) {
            startedFrom = new LongFilter();
        }
        return startedFrom;
    }

    public void setStartedFrom(LongFilter startedFrom) {
        this.startedFrom = startedFrom;
    }

    public LongFilter getEndingIn() {
        return endingIn;
    }

    public LongFilter endingIn() {
        if (endingIn == null) {
            endingIn = new LongFilter();
        }
        return endingIn;
    }

    public void setEndingIn(LongFilter endingIn) {
        this.endingIn = endingIn;
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
        final StationCriteria that = (StationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(startedFrom, that.startedFrom) &&
            Objects.equals(endingIn, that.endingIn) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, startedFrom, endingIn, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StationCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (startedFrom != null ? "startedFrom=" + startedFrom + ", " : "") +
            (endingIn != null ? "endingIn=" + endingIn + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
