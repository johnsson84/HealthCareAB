package health.care.booking.respository;

import health.care.booking.models.TokenPasswordReset;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenPasswordResetRepository  extends MongoRepository<TokenPasswordReset, String> {
    TokenPasswordReset findByToken(String token);
}
