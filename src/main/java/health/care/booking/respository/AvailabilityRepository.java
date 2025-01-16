package health.care.booking.respository;

import health.care.booking.models.Availability;
import health.care.booking.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AvailabilityRepository extends MongoRepository<Availability, String> {
    List<Availability> findAll();
    List<Availability> findByCaregiverId(String caregiverId);

    // Find availability where a specific time is NOT in availableSlots
    @Query("{ 'caregiverId': ?0, 'availableSlots': { $ne: ?1 } }")
    Optional<Availability> findByCaregiverIdAndAvailableSlotsNotContaining(String caregiverId, Date availableSlot);

    Optional<Availability> findById(String id);
}

