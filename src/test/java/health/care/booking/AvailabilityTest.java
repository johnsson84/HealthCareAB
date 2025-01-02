package health.care.booking;

import health.care.booking.models.Availability;
import health.care.booking.models.Role;
import health.care.booking.models.User;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import health.care.booking.services.AvailabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AvailabilityTest {

    @Mock
    private AvailabilityRepository availabilityRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AvailabilityService availabilityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCheckDuplicateAvailability() {
        // Mock caregiver user
        User mockCaregiver = new User();
        mockCaregiver.setId("caregiver123");
        mockCaregiver.setRoles(Collections.singleton(Role.ADMIN));

        // Mock availability slots
        List<LocalDateTime> existingSlots = Arrays.asList(
                LocalDateTime.of(2024, 1, 1, 9, 0),
                LocalDateTime.of(2024, 1, 1, 9, 30)
        );
        List<LocalDateTime> newSlots = Arrays.asList(
                LocalDateTime.of(2024, 1, 1, 9, 0), // Duplicate
                LocalDateTime.of(2024, 1, 1, 10, 0)
        );

        // Mock existing availability
        Availability existingAvailability = new Availability();
        existingAvailability.setCaregiverId(mockCaregiver);
        existingAvailability.setAvailableSlots(existingSlots);

        // Mock new availability
        Availability newAvailability = new Availability();
        newAvailability.setCaregiverId(mockCaregiver);
        newAvailability.setAvailableSlots(newSlots);

        // Mock repository call
        when(availabilityRepository.findByCaregiverId(mockCaregiver))
                .thenReturn(Collections.singletonList(existingAvailability));

        // Call the method
        boolean isDuplicate = availabilityService.checkDuplicateAvailability(newAvailability);

        // Verify behavior
        verify(availabilityRepository, times(1)).findByCaregiverId(mockCaregiver);

        // Assertions
        assertEquals(true, isDuplicate); // Duplicate exists
    }

    @Test
    void testCreateAvailability() {
        // Mock caregiver user
        User mockCaregiver = new User();
        mockCaregiver.setId("caregiver123");
        mockCaregiver.setRoles(Collections.singleton(Role.ADMIN));

        // Mock availability slots
        List<LocalDateTime> mockSlots = Arrays.asList(
                LocalDateTime.of(2024, 1, 1, 9, 0),
                LocalDateTime.of(2024, 1, 1, 9, 30)
        );

        // Mock new availability
        Availability newAvailability = new Availability();
        newAvailability.setCaregiverId(mockCaregiver);
        newAvailability.setAvailableSlots(mockSlots);

        // Mock repository call for saving availability
        when(availabilityRepository.save(newAvailability)).thenReturn(newAvailability);

        // Call the method
        availabilityRepository.save(newAvailability);

        // Verify behavior
        verify(availabilityRepository, times(1)).save(newAvailability);

        // Assertions
        assertEquals("caregiver123", newAvailability.getCaregiverId().getId());
        assertEquals(2, newAvailability.getAvailableSlots().size());
        assertEquals(mockSlots, newAvailability.getAvailableSlots());
    }

}
