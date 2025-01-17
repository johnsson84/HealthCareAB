package health.care.booking.controllers;


import health.care.booking.dto.AvailabilityUserIdRequest;
import health.care.booking.dto.AvailabilityUserIdResponse;
import health.care.booking.dto.UpdateURLRequest;
import health.care.booking.respository.UserRepository;

import health.care.booking.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;
  @Autowired
  private UserRepository userRepository;


  @GetMapping("/find/{username}")
  public ResponseEntity<?> findUserByUsername(@Valid @PathVariable String username) {
    try {
      return ResponseEntity.ok(userService.findByUsername(username));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/find/userURL/{username}")
  public ResponseEntity<?> findUserPictureURL(@Valid @PathVariable String username) {
      String url = userService.findByUsername(username).getUserPictureURL();
      if (url == null || url.isEmpty()) {
          url = "placeholder.jpg";
      }
      try {
          return ResponseEntity.ok(url);
      } catch (Exception e) {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      }
  }


  @GetMapping("/find-userId/{userId}")
  public ResponseEntity<?> findUserById(@Valid @PathVariable String userId) {
    try {
      return ResponseEntity.ok(userService.findByUserId(userId));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

    @GetMapping("/full-name/{userId}")
    public ResponseEntity<?> findFullNameByUserId(@Valid @PathVariable String userId) {

        try {
            return ResponseEntity.ok(userRepository.findById(userId).get().getFirstName() + " " + userRepository.findById(userId).get().getLastName());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> findUserByid(@Valid @PathVariable String userId) {
        try {
            return ResponseEntity.ok(userRepository.findById(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/find/caregivers-by-availability")
    public List<AvailabilityUserIdResponse> findDoctorByAvailability(@Valid @RequestBody AvailabilityUserIdRequest userIdList) {
        if (userIdList.getUserIds() == null || userIdList.getUserIds().isEmpty()) {
            throw new IllegalArgumentException("User ID list cannot be null or empty");
        }
        return userService.makeAndSendBackUserResponse(userIdList.getUserIds());
    }
    @PutMapping("update-user-picture/{username}")
    public ResponseEntity<?> updateUserProfilePicture(@Valid @RequestBody UpdateURLRequest url, @PathVariable String username){
      try {
          userService.findUserAndUpdatePictureUrl(url.getUrl(), username);
      } catch (Exception e) {
          return ResponseEntity.status(400).body(e.getMessage());
      }
      return ResponseEntity.ok("Picture was changed!");
    }


}
