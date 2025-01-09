package health.care.booking.services;

import health.care.booking.models.Role;
import health.care.booking.models.User;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public User registerUser(User user) {
    // hash the password
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    // ensure the user has at least the default role
    if (user.getRoles() == null || user.getRoles().isEmpty()) {
      user.setRoles(Set.of(Role.USER));
    }

    userRepository.save(user);
    return user;
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  public boolean existsByUsername(String username) {
    return userRepository.findByUsername(username).isPresent();
  }

  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public void deleteUser(String userId) {
    userRepository.deleteById(userId);
  }

  public User findByUserId(String userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }
}
