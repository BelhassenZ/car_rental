package org.car_rental.service.impl;

import org.car_rental.service.CarService;
import org.car_rental.domain.Car;
import org.car_rental.repository.CarRepository;
import org.car_rental.repository.search.CarSearchRepository;
import org.car_rental.service.dto.CarDTO;
import org.car_rental.service.mapper.CarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Car.
 */
@Service
@Transactional
public class CarServiceImpl implements CarService {

    private final Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

    private final CarRepository carRepository;

    private final CarMapper carMapper;

    private final CarSearchRepository carSearchRepository;

    public CarServiceImpl(CarRepository carRepository, CarMapper carMapper, CarSearchRepository carSearchRepository) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.carSearchRepository = carSearchRepository;
    }

    /**
     * Save a car.
     *
     * @param carDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CarDTO save(CarDTO carDTO) {
        log.debug("Request to save Car : {}", carDTO);
        Car car = carMapper.toEntity(carDTO);
        car = carRepository.save(car);
        CarDTO result = carMapper.toDto(car);
        carSearchRepository.save(car);
        return result;
    }

    /**
     * Get all the cars.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Cars");
        return carRepository.findAll(pageable)
            .map(carMapper::toDto);
    }


    /**
     *  get all the cars where Concerned is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<CarDTO> findAllWhereConcernedIsNull() {
        log.debug("Request to get all cars where Concerned is null");
        return StreamSupport
            .stream(carRepository.findAll().spliterator(), false)
            .filter(car -> car.getConcerned() == null)
            .map(carMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one car by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CarDTO findOne(Long id) {
        log.debug("Request to get Car : {}", id);
        Car car = carRepository.findOne(id);
        return carMapper.toDto(car);
    }

    /**
     * Delete the car by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Car : {}", id);
        carRepository.delete(id);
        carSearchRepository.delete(id);
    }

    /**
     * Search for the car corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CarDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Cars for query {}", query);
        Page<Car> result = carSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(carMapper::toDto);
    }
}
