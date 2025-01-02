package health.care.booking.respository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import health.care.booking.models.Feedback;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {

}
