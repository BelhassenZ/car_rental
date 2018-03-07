package org.car_rental.repository.search;

import org.car_rental.domain.Booking;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Booking entity.
 */
public interface BookingSearchRepository extends ElasticsearchRepository<Booking, Long> {
}
