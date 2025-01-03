package health.care.booking.services;

import health.care.booking.models.Availability;
import health.care.booking.respository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AvailabilityService {

    @Autowired
    AvailabilityRepository availabilityRepository;

    public List<Date> createWeeklyAvailability() {
        List<LocalDateTime> availabilities = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusWeeks(2);

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek().getValue() >= 1 && date.getDayOfWeek().getValue() <= 5) {
                LocalTime startTime = LocalTime.of(8, 0);
                LocalTime endTime = LocalTime.of(16, 0);

                for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(30)) {
                    availabilities.add(LocalDateTime.of(date, time));
                }
            }
        }
        return availabilities;
    }

    public boolean checkDuplicateAvailability(Availability availability) {
        // Fetch existing availability slots for the given caregiver
        List<Availability> existingAvailabilities = availabilityRepository.findByCaregiverId(availability.getCaregiverId());

        // Check if any of the new slots already exist in the existing slots
        for (Date newAvailability : availability.getAvailableSlots()) {
            for (Availability existingAvailability : existingAvailabilities) {
                if (existingAvailability.getAvailableSlots().contains(newAvailability)) {
                    return true;
                }
            }
        }

        // If no duplicates are found
        return false;
    }
}
