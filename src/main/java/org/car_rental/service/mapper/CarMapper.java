package org.car_rental.service.mapper;

import org.car_rental.domain.*;
import org.car_rental.service.dto.CarDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Car and its DTO CarDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CarMapper extends EntityMapper<CarDTO, Car> {


    @Mapping(target = "concerned", ignore = true)
    Car toEntity(CarDTO carDTO);

    default Car fromId(Long id) {
        if (id == null) {
            return null;
        }
        Car car = new Car();
        car.setId(id);
        return car;
    }
}
