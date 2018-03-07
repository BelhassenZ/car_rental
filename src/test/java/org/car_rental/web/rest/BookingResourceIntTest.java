package org.car_rental.web.rest;

import org.car_rental.CarRentalApp;

import org.car_rental.domain.Booking;
import org.car_rental.repository.BookingRepository;
import org.car_rental.service.BookingService;
import org.car_rental.repository.search.BookingSearchRepository;
import org.car_rental.service.dto.BookingDTO;
import org.car_rental.service.mapper.BookingMapper;
import org.car_rental.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static org.car_rental.web.rest.TestUtil.sameInstant;
import static org.car_rental.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BookingResource REST controller.
 *
 * @see BookingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarRentalApp.class)
public class BookingResourceIntTest {

    private static final Integer DEFAULT_NOOFRENTDAYS = 1;
    private static final Integer UPDATED_NOOFRENTDAYS = 2;

    private static final ZonedDateTime DEFAULT_START_DAY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DAY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DAY = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DAY = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Double DEFAULT_RENT_PER_DAY = 1D;
    private static final Double UPDATED_RENT_PER_DAY = 2D;

    private static final Double DEFAULT_TOTAL_AMOUNT_PAYABLE = 1D;
    private static final Double UPDATED_TOTAL_AMOUNT_PAYABLE = 2D;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingSearchRepository bookingSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBookingMockMvc;

    private Booking booking;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BookingResource bookingResource = new BookingResource(bookingService);
        this.restBookingMockMvc = MockMvcBuilders.standaloneSetup(bookingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createEntity(EntityManager em) {
        Booking booking = new Booking()
            .noofrentdays(DEFAULT_NOOFRENTDAYS)
            .startDay(DEFAULT_START_DAY)
            .endDay(DEFAULT_END_DAY)
            .rentPerDay(DEFAULT_RENT_PER_DAY)
            .totalAmountPayable(DEFAULT_TOTAL_AMOUNT_PAYABLE);
        return booking;
    }

    @Before
    public void initTest() {
        bookingSearchRepository.deleteAll();
        booking = createEntity(em);
    }

    @Test
    @Transactional
    public void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);
        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getNoofrentdays()).isEqualTo(DEFAULT_NOOFRENTDAYS);
        assertThat(testBooking.getStartDay()).isEqualTo(DEFAULT_START_DAY);
        assertThat(testBooking.getEndDay()).isEqualTo(DEFAULT_END_DAY);
        assertThat(testBooking.getRentPerDay()).isEqualTo(DEFAULT_RENT_PER_DAY);
        assertThat(testBooking.getTotalAmountPayable()).isEqualTo(DEFAULT_TOTAL_AMOUNT_PAYABLE);

        // Validate the Booking in Elasticsearch
        Booking bookingEs = bookingSearchRepository.findOne(testBooking.getId());
        assertThat(testBooking.getStartDay()).isEqualTo(testBooking.getStartDay());
        assertThat(testBooking.getEndDay()).isEqualTo(testBooking.getEndDay());
        assertThat(bookingEs).isEqualToIgnoringGivenFields(testBooking, "startDay", "endDay");
    }

    @Test
    @Transactional
    public void createBookingWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // Create the Booking with an existing ID
        booking.setId(1L);
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNoofrentdaysIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setNoofrentdays(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setStartDay(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setEndDay(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRentPerDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setRentPerDay(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalAmountPayableIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setTotalAmountPayable(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc.perform(post("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList
        restBookingMockMvc.perform(get("/api/bookings?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].noofrentdays").value(hasItem(DEFAULT_NOOFRENTDAYS)))
            .andExpect(jsonPath("$.[*].startDay").value(hasItem(sameInstant(DEFAULT_START_DAY))))
            .andExpect(jsonPath("$.[*].endDay").value(hasItem(sameInstant(DEFAULT_END_DAY))))
            .andExpect(jsonPath("$.[*].rentPerDay").value(hasItem(DEFAULT_RENT_PER_DAY.doubleValue())))
            .andExpect(jsonPath("$.[*].totalAmountPayable").value(hasItem(DEFAULT_TOTAL_AMOUNT_PAYABLE.doubleValue())));
    }

    @Test
    @Transactional
    public void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(booking.getId().intValue()))
            .andExpect(jsonPath("$.noofrentdays").value(DEFAULT_NOOFRENTDAYS))
            .andExpect(jsonPath("$.startDay").value(sameInstant(DEFAULT_START_DAY)))
            .andExpect(jsonPath("$.endDay").value(sameInstant(DEFAULT_END_DAY)))
            .andExpect(jsonPath("$.rentPerDay").value(DEFAULT_RENT_PER_DAY.doubleValue()))
            .andExpect(jsonPath("$.totalAmountPayable").value(DEFAULT_TOTAL_AMOUNT_PAYABLE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get("/api/bookings/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        bookingSearchRepository.save(booking);
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking
        Booking updatedBooking = bookingRepository.findOne(booking.getId());
        // Disconnect from session so that the updates on updatedBooking are not directly saved in db
        em.detach(updatedBooking);
        updatedBooking
            .noofrentdays(UPDATED_NOOFRENTDAYS)
            .startDay(UPDATED_START_DAY)
            .endDay(UPDATED_END_DAY)
            .rentPerDay(UPDATED_RENT_PER_DAY)
            .totalAmountPayable(UPDATED_TOTAL_AMOUNT_PAYABLE);
        BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

        restBookingMockMvc.perform(put("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getNoofrentdays()).isEqualTo(UPDATED_NOOFRENTDAYS);
        assertThat(testBooking.getStartDay()).isEqualTo(UPDATED_START_DAY);
        assertThat(testBooking.getEndDay()).isEqualTo(UPDATED_END_DAY);
        assertThat(testBooking.getRentPerDay()).isEqualTo(UPDATED_RENT_PER_DAY);
        assertThat(testBooking.getTotalAmountPayable()).isEqualTo(UPDATED_TOTAL_AMOUNT_PAYABLE);

        // Validate the Booking in Elasticsearch
        Booking bookingEs = bookingSearchRepository.findOne(testBooking.getId());
        assertThat(testBooking.getStartDay()).isEqualTo(testBooking.getStartDay());
        assertThat(testBooking.getEndDay()).isEqualTo(testBooking.getEndDay());
        assertThat(bookingEs).isEqualToIgnoringGivenFields(testBooking, "startDay", "endDay");
    }

    @Test
    @Transactional
    public void updateNonExistingBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restBookingMockMvc.perform(put("/api/bookings")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        bookingSearchRepository.save(booking);
        int databaseSizeBeforeDelete = bookingRepository.findAll().size();

        // Get the booking
        restBookingMockMvc.perform(delete("/api/bookings/{id}", booking.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean bookingExistsInEs = bookingSearchRepository.exists(booking.getId());
        assertThat(bookingExistsInEs).isFalse();

        // Validate the database is empty
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);
        bookingSearchRepository.save(booking);

        // Search the booking
        restBookingMockMvc.perform(get("/api/_search/bookings?query=id:" + booking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(booking.getId().intValue())))
            .andExpect(jsonPath("$.[*].noofrentdays").value(hasItem(DEFAULT_NOOFRENTDAYS)))
            .andExpect(jsonPath("$.[*].startDay").value(hasItem(sameInstant(DEFAULT_START_DAY))))
            .andExpect(jsonPath("$.[*].endDay").value(hasItem(sameInstant(DEFAULT_END_DAY))))
            .andExpect(jsonPath("$.[*].rentPerDay").value(hasItem(DEFAULT_RENT_PER_DAY.doubleValue())))
            .andExpect(jsonPath("$.[*].totalAmountPayable").value(hasItem(DEFAULT_TOTAL_AMOUNT_PAYABLE.doubleValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Booking.class);
        Booking booking1 = new Booking();
        booking1.setId(1L);
        Booking booking2 = new Booking();
        booking2.setId(booking1.getId());
        assertThat(booking1).isEqualTo(booking2);
        booking2.setId(2L);
        assertThat(booking1).isNotEqualTo(booking2);
        booking1.setId(null);
        assertThat(booking1).isNotEqualTo(booking2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingDTO.class);
        BookingDTO bookingDTO1 = new BookingDTO();
        bookingDTO1.setId(1L);
        BookingDTO bookingDTO2 = new BookingDTO();
        assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
        bookingDTO2.setId(bookingDTO1.getId());
        assertThat(bookingDTO1).isEqualTo(bookingDTO2);
        bookingDTO2.setId(2L);
        assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
        bookingDTO1.setId(null);
        assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(bookingMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(bookingMapper.fromId(null)).isNull();
    }
}
