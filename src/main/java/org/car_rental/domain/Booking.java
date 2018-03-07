package org.car_rental.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "booking")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 1)
    @Column(name = "noofrentdays", nullable = false)
    private Integer noofrentdays;

    @NotNull
    @Column(name = "start_day", nullable = false)
    private ZonedDateTime startDay;

    @NotNull
    @Column(name = "end_day", nullable = false)
    private ZonedDateTime endDay;

    @NotNull
    @Column(name = "rent_per_day", nullable = false)
    private Double rentPerDay;

    @NotNull
    @Column(name = "total_amount_payable", nullable = false)
    private Double totalAmountPayable;

    @OneToOne
    @JoinColumn(unique = true)
    private Car concerns;

    @ManyToOne
    private Customer done;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNoofrentdays() {
        return noofrentdays;
    }

    public Booking noofrentdays(Integer noofrentdays) {
        this.noofrentdays = noofrentdays;
        return this;
    }

    public void setNoofrentdays(Integer noofrentdays) {
        this.noofrentdays = noofrentdays;
    }

    public ZonedDateTime getStartDay() {
        return startDay;
    }

    public Booking startDay(ZonedDateTime startDay) {
        this.startDay = startDay;
        return this;
    }

    public void setStartDay(ZonedDateTime startDay) {
        this.startDay = startDay;
    }

    public ZonedDateTime getEndDay() {
        return endDay;
    }

    public Booking endDay(ZonedDateTime endDay) {
        this.endDay = endDay;
        return this;
    }

    public void setEndDay(ZonedDateTime endDay) {
        this.endDay = endDay;
    }

    public Double getRentPerDay() {
        return rentPerDay;
    }

    public Booking rentPerDay(Double rentPerDay) {
        this.rentPerDay = rentPerDay;
        return this;
    }

    public void setRentPerDay(Double rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public Double getTotalAmountPayable() {
        return totalAmountPayable;
    }

    public Booking totalAmountPayable(Double totalAmountPayable) {
        this.totalAmountPayable = totalAmountPayable;
        return this;
    }

    public void setTotalAmountPayable(Double totalAmountPayable) {
        this.totalAmountPayable = totalAmountPayable;
    }

    public Car getConcerns() {
        return concerns;
    }

    public Booking concerns(Car car) {
        this.concerns = car;
        return this;
    }

    public void setConcerns(Car car) {
        this.concerns = car;
    }

    public Customer getDone() {
        return done;
    }

    public Booking done(Customer customer) {
        this.done = customer;
        return this;
    }

    public void setDone(Customer customer) {
        this.done = customer;
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
        Booking booking = (Booking) o;
        if (booking.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), booking.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Booking{" +
            "id=" + getId() +
            ", noofrentdays=" + getNoofrentdays() +
            ", startDay='" + getStartDay() + "'" +
            ", endDay='" + getEndDay() + "'" +
            ", rentPerDay=" + getRentPerDay() +
            ", totalAmountPayable=" + getTotalAmountPayable() +
            "}";
    }
}
