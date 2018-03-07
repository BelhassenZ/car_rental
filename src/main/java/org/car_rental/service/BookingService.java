package org.car_rental.service;

import org.car_rental.service.dto.BookingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Booking.
 */
public interface BookingService {

    /**
     * Save a booking.
     *
     * @param bookingDTO the entity to save
     * @return the persisted entity
     */
    BookingDTO save(BookingDTO bookingDTO);

    /**
     * Get all the bookings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BookingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" booking.
     *
     * @param id the id of the entity
     * @return the entity
     */
    BookingDTO findOne(Long id);

    /**
     * Delete the "id" booking.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the booking corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BookingDTO> search(String query, Pageable pageable);
}
