package health.care.booking.services;

import health.care.booking.models.Availability;
import health.care.booking.models.Status;
import health.care.booking.models.User;
import health.care.booking.respository.AvailabilityRepository;
import health.care.booking.respository.UserRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AvailabilityRepository availabilityRepository;

    public User setPatient(String username){
        User patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("No user found with that username."));
        return patient;
    }
    public User setCaregiver(String caregiverId) {
        User caregiver = userRepository.findById(caregiverId)
                .orElseThrow(() -> new RuntimeException("No user found with that Id."));
        return caregiver;
    }

    public void removeAvailabilitySlots(String availabilityId, @NotNull LocalDateTime availabilityDate){
        Availability removeAvailability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new RuntimeException("Could not find availability object."));
        LocalDateTime removeThis = availabilityDate;

        for (int i = 0; i < removeAvailability.getAvailableSlots().size(); i++) {
            if (removeAvailability.getAvailableSlots().get(i).equals(removeThis)){
                removeAvailability.getAvailableSlots().remove(i);
                System.out.println("removed: "  + removeThis);
                availabilityRepository.save(removeAvailability);
            }
        }
    }
    public Status returnStatus(String status){
        if (status.toUpperCase().equals(Status.CANCELLED.name())){
           return Status.CANCELLED;
        } else if (status.toUpperCase().equals(Status.COMPLETED.name())) {
            return Status.COMPLETED;
        }else {
            return Status.ERROR;
        }
    }
}
