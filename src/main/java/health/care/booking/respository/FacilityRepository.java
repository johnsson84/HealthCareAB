package health.care.booking.respository;

import health.care.booking.models.Facility;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FacilityRepository  extends MongoRepository<Facility, String> {
}
