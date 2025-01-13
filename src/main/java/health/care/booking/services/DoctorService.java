package health.care.booking.services;

import health.care.booking.models.Doctor;
import health.care.booking.respository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Doctor registerDoctor(Doctor doctor) {
        // hash the password
        String encodedPassword = passwordEncoder.encode(doctor.getPassword());
        doctor.setPassword(encodedPassword);


        doctorRepository.save(doctor);
        return doctor;
    }

    public Doctor findByUsername(String username) {
        return doctorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


}
