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
    void testAddAvailabilityWithCaregiver() {
        // Mock caregiver user
        User mockCaregiver = new User();
        mockCaregiver.setId("caregiver123");
        mockCaregiver.setRoles(Collections.singleton(Role.ADMIN));

        // Mock availability slots
        List<LocalDateTime> mockSlots = Arrays.asList(
                LocalDateTime.of(2024, 1, 1, 9, 0),
                LocalDateTime.of(2024, 1, 1, 9, 30)
        );

        // Mock availability
        Availability mockAvailability = new Availability();
        mockAvailability.setCaregiverId(mockCaregiver);
        mockAvailability.setAvailableSlots(mockSlots);

        // Mock repository calls
        when(userRepository.findUserByRolesIs(Collections.singleton(Role.ADMIN)))
                .thenReturn(Collections.singletonList(mockCaregiver));
        when(availabilityRepository.findByCaregiverId(mockCaregiver))
                .thenReturn(Collections.emptyList());
        doNothing().when(availabilityRepository).save(any(Availability.class));

        // Call the method
        List<LocalDateTime> result = availabilityService.createWeeklyAvailability();
        mockAvailability.setAvailableSlots(result);

        boolean response = availabilityService.checkDuplicateAvailability(mockAvailability);

        // Verify behavior
        verify(userRepository, times(1)).findUserByRolesIs(Collections.singleton(Role.ADMIN));
        verify(availabilityRepository, times(1)).findByCaregiverId(mockCaregiver);
        verify(availabilityRepository, times(1)).save(mockAvailability);

        // Assertions
        assertEquals(ResponseEntity.ok("No duplicate availability found."), response);
    }
}
