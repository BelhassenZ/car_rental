package org.car_rental.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "car")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "model", nullable = false)
    private String model;

    @NotNull
    @Column(name = "jhi_type", nullable = false)
    private String type;

    @NotNull
    @Column(name = "reg_number", nullable = false)
    private String regNumber;

    @NotNull
    @Column(name = "available", nullable = false)
    private Boolean available;

    @OneToOne(mappedBy = "concerns")
    @JsonIgnore
    private Booking concerned;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public Car model(String model) {
        this.model = model;
        return this;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public Car type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public Car regNumber(String regNumber) {
        this.regNumber = regNumber;
        return this;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Boolean isAvailable() {
        return available;
    }

    public Car available(Boolean available) {
        this.available = available;
        return this;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Booking getConcerned() {
        return concerned;
    }

    public Car concerned(Booking booking) {
        this.concerned = booking;
        return this;
    }

    public void setConcerned(Booking booking) {
        this.concerned = booking;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        if (car.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), car.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", model='" + getModel() + "'" +
            ", type='" + getType() + "'" +
            ", regNumber='" + getRegNumber() + "'" +
            ", available='" + isAvailable() + "'" +
            "}";
    }
}
