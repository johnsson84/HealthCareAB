package health.care.booking.services;

import health.care.booking.models.Availability;
import health.care.booking.models.User;
import health.care.booking.respository.AvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.*;
@Service
public class AvailabilityService {

    @Autowired
    AvailabilityRepository availabilityRepository;

    public Availability createNewAvailability(User caregiver) {
        Availability newAvailability = new Availability();
        newAvailability.setCaregiverId(caregiver);
        newAvailability.setAvailableSlots(createWeeklyAvailabilitySlots());
        checkDuplicateAvailability(newAvailability);
        return newAvailability;
    }

    public boolean loopCaregiverList(List<User> caregiverList){
        List<Availability> availabilities = new ArrayList<>();
        if (caregiverList.isEmpty()) {
            throw new RuntimeException("Couldn't find any caregivers");
        }

        for (User user : caregiverList) {
            Availability availability = new Availability();
            availability.setAvailableSlots(createWeeklyAvailabilitySlots());
            availability.setCaregiverId(user);
            if (!checkDuplicateAvailability(availability)) {
                availabilities.add(availability);
            }
            availabilityRepository.saveAll(availabilities);
        }
        return true;
    }
        public List<Date> createWeeklyAvailabilitySlots() {
            List<Date> availabilities = new ArrayList<>();
            LocalDate startDate = LocalDate.now();
            LocalDate endDate = LocalDate.now().plusWeeks(2);

            for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
                // Check if the day is Monday (1) to Friday (5)
                if (date.getDayOfWeek().getValue() >= 1 && date.getDayOfWeek().getValue() <= 5) {
                    LocalTime startTime = LocalTime.of(8, 0);
                    LocalTime endTime = LocalTime.of(16, 0);

                    for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(30)) {
                        // Convert LocalDate and LocalTime to Date
                        Date availabilityDate = Date.from(date.atTime(time).atZone(ZoneId.systemDefault()).toInstant());
                        availabilities.add(availabilityDate);
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

    public void removeAvailabilityByArray(List<Date> changingDates, Availability changingDatesAvailability) {
        if (changingDates != null && changingDatesAvailability != null) {
            changingDatesAvailability.getAvailableSlots().removeAll(changingDates);
        } else throw new RuntimeException("changingDates List is null or changingDatesAvailability");
        availabilityRepository.save(changingDatesAvailability);
    }
}
