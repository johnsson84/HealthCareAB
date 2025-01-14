package health.care.booking.controllers;

import health.care.booking.dto.FacilityRequest;
import health.care.booking.models.Facility;
import health.care.booking.models.User;
import health.care.booking.services.FacilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facility")
public class FacilityController {

    @Autowired
    private FacilityService facilityService;

    @PostMapping("/create")
    public ResponseEntity<?> createFacility(@RequestBody FacilityRequest facilityRequest) {
        Facility createdFacility = facilityService.createFacility(facilityRequest);
        return ResponseEntity.ok("A new facility was added");
    }
}
