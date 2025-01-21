package health.care.booking.respository;

import health.care.booking.models.Conditions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConditionsRepository extends MongoRepository<Conditions, String> {
}
