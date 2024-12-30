package health.care.booking.respository;

import health.care.booking.models.Availability;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends MongoRepository<Availability, String> {
    List<Availability> findAll();
}
