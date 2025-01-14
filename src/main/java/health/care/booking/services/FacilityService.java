package health.care.booking.services;
import health.care.booking.dto.CoworkerRequest;
import health.care.booking.dto.FacilityRequest;
import health.care.booking.models.Facility;
import health.care.booking.models.FacilityAddress;
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

    public Facility createFacility(FacilityRequest facilityRequest) {

        Facility newFacility = new Facility();
        newFacility.setFacilityName(facilityRequest.getFacilityName());
        newFacility.setAddress(facilityRequest.getFacilityAddress());
        newFacility.setPhoneNumber(facilityRequest.getPhoneNumber());
        newFacility.setEmail(facilityRequest.getEmail());
        newFacility.setHoursOpen(facilityRequest.getHoursOpen());

//        // Fetch coworkers based on username
//        List<User> coworkers = facilityRequest.getCoworkers().stream()
//                .map(coworker -> userRepository.findByUsername(coworker.getUsername())
//                        .orElseThrow(() -> new IllegalArgumentException(
//                                "User not found with username: " + coworker.getUsername())))
//
//                .collect(Collectors.toList());

        List<CoworkerRequest> coworkers = facilityRequest.getCoworkers().stream()
                .map(coworkerRequest -> {
                    User user = userRepository.findByUsername(coworkerRequest.getUsername())
                            .orElseThrow(() -> new IllegalArgumentException("User not found: " + coworkerRequest.getUsername()));
                    return new CoworkerRequest(
                            user.getUsername(),
                            user.getMail(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getRoles().stream().findFirst().orElse(null) // Assuming a single role
                    );
                })
                .collect(Collectors.toList());

        newFacility.setCoworkers(coworkers);
        return facilityRepository.save(newFacility);
    }

    public List<Facility> getAllFacilities() {
        return facilityRepository.findAll();
    }

    public Facility getFacilityById(String id) {
        return facilityRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Facility not found"));
    }
}
