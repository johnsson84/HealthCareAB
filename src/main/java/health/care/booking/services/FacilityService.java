package health.care.booking.services;
import health.care.booking.dto.FacilityRequest;
import health.care.booking.dto.UserResponse;
import health.care.booking.models.Facility;
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
       // Hämtar info om cowokers baserat på ID
        List<String> coworkerIds = facilityRequest.getCoworkersId().stream()
                .map(username -> userRepository.findByUsername(username)
                        .orElseThrow(() -> new IllegalArgumentException("User not found " + username))
                        .getId())
                .toList();
        newFacility.setCoworkers(coworkerIds);
        return facilityRepository.save(newFacility);
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
                    response.setUsernameDto(user.getUsername());
                    response.setEmailDto(user.getMail());
                    response.setFirstNameDto(user.getFirstName());
                    response.setLastNameDto(user.getLastName());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public Facility GetFacilityById(String id) {
        return facilityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Facility not found"));
    }

    public Facility DeleteFacilityById(String id) {
        Facility facility = facilityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Facility not found"));
        facilityRepository.delete(facility);
        return facility;
    }
}
