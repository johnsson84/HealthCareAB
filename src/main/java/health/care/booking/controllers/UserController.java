package health.care.booking.controllers;

import health.care.booking.dto.AuthRequest;
import health.care.booking.dto.AvailabilityUserIdRequest;
import health.care.booking.dto.AvailabilityUserIdResponse;
import health.care.booking.models.User;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.CustomUserDetailsService;
import health.care.booking.services.UserService;
import health.care.booking.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/find/{username}")
    public ResponseEntity<?> findUserByUsername(@Valid @PathVariable String username) {
        try {
            return ResponseEntity.ok(userService.findByUsername(username));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/find/caregivers-by-availability")
    public List<AvailabilityUserIdResponse> findUserByAvailabilityUserId(@Valid @RequestBody AvailabilityUserIdRequest userIdList) {
        if (userIdList.getUserIds() == null || userIdList.getUserIds().isEmpty()) {
            throw new IllegalArgumentException("User ID list cannot be null or empty");
        }
        List<AvailabilityUserIdResponse> idResponses = userService.makeAndSendBackUserResponse(userIdList.getUserIds());
        return idResponses;

    }
}
