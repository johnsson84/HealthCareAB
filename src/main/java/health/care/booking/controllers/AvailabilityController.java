package health.care.booking.controllers;

import health.care.booking.dto.AuthRequest;
import health.care.booking.dto.AvailabilityRequest;
import health.care.booking.models.Availability;
import health.care.booking.models.Role;
import health.care.booking.models.User;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AvailabilityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
            if (!availabilityService.checkDuplicateAvailability(availability)) {
                availabilityRepository.save(availability);
            } else {
                System.out.println("User " + availability.getCaregiverId() + " has duplicate availability");
            }

        }
        return ResponseEntity.ok("All caregivers availability have been set.");
    }

    @PostMapping("/set/one")
    public ResponseEntity<?> setAvailabilityForOne(@Valid @RequestBody AvailabilityRequest availabilityRequest) {
        User careGiver = userRepository.findById(availabilityRequest.careGiverId)
                .orElseThrow(() -> new RuntimeException("Hitta inte"));

        Availability newAvailability = new Availability();

        newAvailability.setCaregiverId(careGiver);
        newAvailability.setAvailableSlots(availabilityService.createWeeklyAvailability());
        if (!availabilityService.checkDuplicateAvailability(newAvailability)) {
            availabilityRepository.save(newAvailability);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There are duplicates");
        }
        return ResponseEntity.ok("Added available times for user: " + careGiver.getUsername());
    }

    @GetMapping
    public List<Availability> getAllAvailability() {
        return availabilityRepository.findAll();
    }
}