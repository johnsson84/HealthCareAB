package health.care.booking.services;
import health.care.booking.dto.FacilityRequest;
import health.care.booking.dto.UserResponse;
import health.care.booking.models.Facility;
import health.care.booking.models.Role;
import health.care.booking.models.User;
import health.care.booking.respository.FacilityRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityService {

    @Autowired
    private FacilityRepository facilityRepository;
    @Autowired
    private UserRepository userRepository;

    // Reviewer: Det är nog bättre att bryta ut CoworkerRequest till senare imo, det är jävligt mycket data som skickas med annars
    // DONE: John
    public Facility createFacility(FacilityRequest facilityRequest) {
        Facility newFacility = new Facility();
        newFacility.setFacilityName(facilityRequest.getFacilityName());
        newFacility.setAddress(facilityRequest.getFacilityAddress());
        newFacility.setPhoneNumber(facilityRequest.getPhoneNumber());
        newFacility.setEmail(facilityRequest.getEmail());
        newFacility.setHoursOpen(facilityRequest.getHoursOpen());
       // Hämtar info om cowokers baserat på ID, kollar att role inte är USER
        return updateCoworkersToFacility(facilityRequest, newFacility);
    }

    // hämta facilitet med detaljer om coworkers
    public List<UserResponse> getFacilityCoworkers(String facilityId) {
        // Hämta facility via ID
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found: " + facilityId));
        // Hämta detaljer om kollegorna baserat på deras ID
        return facility.getCoworkersId().stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId)))
                .map(user -> {
                    // Omvandla User till UserResponse
                    UserResponse response = new UserResponse();
                    response.setCoworkerUsername(user.getUsername());
                    response.setCoworkerEmail(user.getMail());
                    response.setCoworkerFirstName(user.getFirstName());
                    response.setCoworkerLastName(user.getLastName());
                    response.setCoworkerRole(user.getRoles().stream()
                            .map(Enum::name)
                            .collect(Collectors.joining(", ")));
                    return response;
                })
                .collect(Collectors.toList());
    }

    public Facility updateFacility(String facilityId, FacilityRequest facilityRequest) {
        Facility existingFacility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new IllegalArgumentException("Facility not found: " + facilityId));

        existingFacility.setFacilityName(facilityRequest.getFacilityName());
        existingFacility.setAddress(facilityRequest.getFacilityAddress());
        existingFacility.setPhoneNumber(facilityRequest.getPhoneNumber());
        existingFacility.setEmail(facilityRequest.getEmail());
        existingFacility.setHoursOpen(facilityRequest.getHoursOpen());
        return updateCoworkersToFacility(facilityRequest, existingFacility);
    }

    private Facility updateCoworkersToFacility(FacilityRequest facilityRequest, Facility existingFacility) {
        List<String> updatedCoworkers = facilityRequest.getCoworkersId().stream()
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
                    if (user.getRoles().contains(Role.USER)) {
                        throw new IllegalArgumentException("Invalid role for user: " + user.getUsername() + ". Role: " + user.getRoles());
                    }
                    return user.getId();
                })
                .toList();
        existingFacility.setCoworkers(updatedCoworkers);
        return facilityRepository.save(existingFacility);
    }


    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public Facility GetFacilityById(String id) {
        return facilityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Facility not found"));
    }

    public void DeleteFacilityById(String id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Facility not found"));
        facilityRepository.delete(facility);
    }
}
