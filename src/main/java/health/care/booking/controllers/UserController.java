package health.care.booking.controllers;

import health.care.booking.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserService userService;


  @GetMapping("/find/{username}")
  public ResponseEntity<?> findUserByUsername(@Valid @PathVariable String username) {
    try {
      return ResponseEntity.ok(userService.findByUsername(username));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @GetMapping("/find-userId/{userId}")
  public ResponseEntity<?> findUserById(@Valid @PathVariable String userId) {
    try {
      return ResponseEntity.ok(userService.findByUsername(userId));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }
}
