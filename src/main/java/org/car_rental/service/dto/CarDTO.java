package org.car_rental.service.dto;


import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Car entity.
 */
public class CarDTO implements Serializable {

    private Long id;

    @NotNull
    private String model;

    @NotNull
    private String type;

    @NotNull
    private String regNumber;

    @NotNull
    private Boolean available;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CarDTO carDTO = (CarDTO) o;
        if(carDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), carDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CarDTO{" +
            "id=" + getId() +
            ", model='" + getModel() + "'" +
            ", type='" + getType() + "'" +
            ", regNumber='" + getRegNumber() + "'" +
            ", available='" + isAvailable() + "'" +
            "}";
    }
}
