package org.car_rental.service.impl;

import org.car_rental.service.BookingService;
import org.car_rental.domain.Booking;
import org.car_rental.repository.BookingRepository;
import org.car_rental.repository.search.BookingSearchRepository;
import org.car_rental.service.dto.BookingDTO;
import org.car_rental.service.mapper.BookingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Booking.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final BookingSearchRepository bookingSearchRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper, BookingSearchRepository bookingSearchRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.bookingSearchRepository = bookingSearchRepository;
    }

    /**
     * Save a booking.
     *
     * @param bookingDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BookingDTO save(BookingDTO bookingDTO) {
        log.debug("Request to save Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        BookingDTO result = bookingMapper.toDto(booking);
        bookingSearchRepository.save(booking);
        return result;
    }

    /**
     * Get all the bookings.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bookings");
        return bookingRepository.findAll(pageable)
            .map(bookingMapper::toDto);
    }

    /**
     * Get one booking by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BookingDTO findOne(Long id) {
        log.debug("Request to get Booking : {}", id);
        Booking booking = bookingRepository.findOne(id);
        return bookingMapper.toDto(booking);
    }

    /**
     * Delete the booking by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Booking : {}", id);
        bookingRepository.delete(id);
        bookingSearchRepository.delete(id);
    }

    /**
     * Search for the booking corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BookingDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Bookings for query {}", query);
        Page<Booking> result = bookingSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(bookingMapper::toDto);
    }
}
