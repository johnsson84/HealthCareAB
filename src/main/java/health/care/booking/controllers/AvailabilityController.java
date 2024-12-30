package health.care.booking.controllers;

import health.care.booking.models.Availability;
import health.care.booking.models.Role;
import health.care.booking.models.User;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/availability")
public class AvailabilityController {
    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    AvailabilityService availabilityService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/set/all")
    public ResponseEntity<?> setAvailabilityAll() {
        List<User> caregiverList = userRepository.findUserByRolesIs(Collections.singleton(Role.ADMIN));

        if (caregiverList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Couldn't find any caregivers");
        }
        for (int i = 0; i < caregiverList.size(); i++) {
            Availability availability = new Availability();
            availability.setAvailableSlots(availabilityService.createWeeklyAvailability());
            availability.setCaregiverId(caregiverList.get(i));
            availabilityRepository.save(availability);
        }
        return ResponseEntity.ok("All caregivers appointments have been set.");
    }
}
