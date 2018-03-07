package org.car_rental.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Booking entity.
 */
public class BookingDTO implements Serializable {

    private Long id;

    @NotNull
    @Min(value = 1)
    private Integer noofrentdays;

    @NotNull
    private ZonedDateTime startDay;

    @NotNull
    private ZonedDateTime endDay;

    @NotNull
    private Double rentPerDay;

    @NotNull
    private Double totalAmountPayable;

    private Long concernsId;

    private Long doneId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNoofrentdays() {
        return noofrentdays;
    }

    public void setNoofrentdays(Integer noofrentdays) {
        this.noofrentdays = noofrentdays;
    }

    public ZonedDateTime getStartDay() {
        return startDay;
    }

    public void setStartDay(ZonedDateTime startDay) {
        this.startDay = startDay;
    }

    public ZonedDateTime getEndDay() {
        return endDay;
    }

    public void setEndDay(ZonedDateTime endDay) {
        this.endDay = endDay;
    }

    public Double getRentPerDay() {
        return rentPerDay;
    }

    public void setRentPerDay(Double rentPerDay) {
        this.rentPerDay = rentPerDay;
    }

    public Double getTotalAmountPayable() {
        return totalAmountPayable;
    }

    public void setTotalAmountPayable(Double totalAmountPayable) {
        this.totalAmountPayable = totalAmountPayable;
    }

    public Long getConcernsId() {
        return concernsId;
    }

    public void setConcernsId(Long carId) {
        this.concernsId = carId;
    }

    public Long getDoneId() {
        return doneId;
    }

    public void setDoneId(Long customerId) {
        this.doneId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BookingDTO bookingDTO = (BookingDTO) o;
        if(bookingDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bookingDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BookingDTO{" +
            "id=" + getId() +
            ", noofrentdays=" + getNoofrentdays() +
            ", startDay='" + getStartDay() + "'" +
            ", endDay='" + getEndDay() + "'" +
            ", rentPerDay=" + getRentPerDay() +
            ", totalAmountPayable=" + getTotalAmountPayable() +
            "}";
    }
}
