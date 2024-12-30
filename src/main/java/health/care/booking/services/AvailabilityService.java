package health.care.booking.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AvailabilityService {

    public List<LocalDateTime> createWeeklyAvailability() {
        List<LocalDateTime> availabilities = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusWeeks(1);

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek().getValue() >= 1 && date.getDayOfWeek().getValue() <= 5) {
                LocalTime startTime = LocalTime.of(9, 0);
                LocalTime endTime = LocalTime.of(16, 1);

                for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(30)) {
                    availabilities.add(LocalDateTime.of(date, time));
                }
            }
        }
        return availabilities;
    }
}
