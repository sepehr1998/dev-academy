package fi.solita.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A Station.
 */
@Entity
@Table(name = "station")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StationImport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "started_from")
    private Long startedFrom;

    @Column(name = "ending_in")
    private Long endingIn;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StationImport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public StationImport name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public StationImport address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getStartedFrom() {
        return this.startedFrom;
    }

    public StationImport startedFrom(Long startedFrom) {
        this.setStartedFrom(startedFrom);
        return this;
    }

    public void setStartedFrom(Long startedFrom) {
        this.startedFrom = startedFrom;
    }

    public Long getEndingIn() {
        return this.endingIn;
    }

    public StationImport endingIn(Long endingIn) {
        this.setEndingIn(endingIn);
        return this;
    }

    public void setEndingIn(Long endingIn) {
        this.endingIn = endingIn;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StationImport)) {
            return false;
        }
        return id != null && id.equals(((StationImport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Station{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", startedFrom=" + getStartedFrom() +
            ", endingIn=" + getEndingIn() +
            "}";
    }
}
