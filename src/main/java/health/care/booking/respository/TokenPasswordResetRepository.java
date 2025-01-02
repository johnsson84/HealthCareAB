package health.care.booking.respository;

import health.care.booking.models.TokenPasswordReset;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TokenPasswordResetRepository  extends MongoRepository<TokenPasswordReset, String> {
    Optional<TokenPasswordReset> findByToken(String token);
}
