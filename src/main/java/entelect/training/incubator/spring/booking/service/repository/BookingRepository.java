package entelect.training.incubator.spring.booking.service.repository;

import entelect.training.incubator.spring.booking.service.model.Booking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends CrudRepository<Booking, Integer> {

    Optional<List<Booking>> findByCustomerId(Integer customerId);
}
