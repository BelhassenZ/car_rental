package org.car_rental.service.mapper;

import org.car_rental.domain.*;
import org.car_rental.service.dto.BookingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Booking and its DTO BookingDTO.
 */
@Mapper(componentModel = "spring", uses = {CarMapper.class, CustomerMapper.class})
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {

    @Mapping(source = "concerns.id", target = "concernsId")
    @Mapping(source = "done.id", target = "doneId")
    BookingDTO toDto(Booking booking);

    @Mapping(source = "concernsId", target = "concerns")
    @Mapping(source = "doneId", target = "done")
    Booking toEntity(BookingDTO bookingDTO);

    default Booking fromId(Long id) {
        if (id == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }
}
