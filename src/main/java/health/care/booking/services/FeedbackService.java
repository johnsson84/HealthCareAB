package health.care.booking.services;

import health.care.booking.dto.FeedbackAverageGradeAllResponse;
import health.care.booking.dto.FeedbackDTO;
import health.care.booking.dto.FeedbackHighRating;
import health.care.booking.models.*;
import health.care.booking.respository.AppointmentRepository;
import health.care.booking.respository.FeedbackRepository;
import health.care.booking.respository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public FeedbackService(FeedbackRepository feedbackRepository, AppointmentRepository appointmentRepository, UserRepository userRepository, UserService userService) {
        this.feedbackRepository = feedbackRepository;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // Create a feedback
    public Feedback addFeedback(FeedbackDTO feedbackDTO) throws Exception {
        Appointment appointment = appointmentRepository.findById(feedbackDTO.getAppointmentId())
                .orElseThrow(() -> new Exception("Appointment not found!"));

        // Check if appointment is completed
        if (!appointment.getStatus().equals(Status.COMPLETED))  {
            throw new Exception("Appointment status is not set to COMPLETED");
        }

        String patientUsername = userRepository.findById(appointment.getPatientId())
                .map(User::getUsername)
                .orElseThrow(() -> new Exception("Patient not found!"));

        String caregiverUsername = userRepository.findById(appointment.getCaregiverId())
                .map(User::getUsername)
                .orElseThrow(() -> new Exception("Caregiver not found!"));

        // Check if feedback already given
        List<Feedback> all = getFeedbackForDoctor(caregiverUsername);
        for (Feedback feedback : all) {
            if (feedback.getAppointmentId().equals(feedbackDTO.getAppointmentId())) {
                throw new Exception("Feedback already given!");
            }
        }

        // Create new feedback
        Feedback newFeedback = new Feedback();
        newFeedback.setAppointmentId(feedbackDTO.getAppointmentId());
        newFeedback.setCaregiverUsername(Optional.ofNullable(caregiverUsername).orElse(""));
        newFeedback.setPatientUsername(Optional.ofNullable(patientUsername).orElse(""));
        newFeedback.setComment(Optional.ofNullable(feedbackDTO.getComment()).orElse(""));
        newFeedback.setRating(Optional.of(feedbackDTO.getRating()).orElse(3));
        feedbackRepository.save(newFeedback);
        return newFeedback;
    }

    // Get a list of all feedback for a doctor
    public List<Feedback> getFeedbackForDoctor(String username) throws Exception {
        User doctor = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Doctor not found!"));
        if (doctor.getRoles().contains(Role.DOCTOR)) {
            return feedbackRepository.findAllByCaregiverUsername(username);
        } else {
            throw new Exception("User not a doctor!");
        }
    }

    // Get a list of all feedbacks given from a patient
    public List<Feedback> getPatientFeedbackGiven(String username) throws Exception {
        User patient = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Patient not found!"));
        if (patient.getRoles().contains(Role.USER)) {
            return feedbackRepository.findAllByPatientUsername(username);
        } else {
            throw new Exception("User not a patient!");
        }
    }

    // Delete a feedback
    public ResponseEntity<?> deleteFeedback(String feedbackId) throws Exception {
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new Exception("Feedback does not exist, can't delete..."));
        feedbackRepository.delete(feedback);
        return ResponseEntity.ok("Feedback deleted successfully");
    }

    public double getAverageFeedbackGrade(String username) throws Exception {
        List<Feedback> feedbackList = getFeedbackForDoctor(username);
        int feedbackGradeSum = 0;

        for (Feedback feedback : feedbackList) {
            feedbackGradeSum = feedbackGradeSum + feedback.getRating();
        }
        double feedbackGradeAverage = 0;
        if (feedbackGradeSum == 0){
            return feedbackGradeAverage;
        }else {
            return (double) feedbackGradeSum /feedbackList.size();
        }
    }

    public List<FeedbackAverageGradeAllResponse> getAverageFeedbackGradeAll() throws Exception {
        List<User> careGiverList = userRepository.findUserByRolesIs(Collections.singleton(Role.DOCTOR));
        List<FeedbackAverageGradeAllResponse> usernameAndAverageGrade = new ArrayList<>();

        for (User user : careGiverList) {
            FeedbackAverageGradeAllResponse response = new FeedbackAverageGradeAllResponse();
            response.setUsername(user.getUsername());
            response.setAverageGrade(getAverageFeedbackGrade(user.getUsername()));
            usernameAndAverageGrade.add(response);
        }

        return usernameAndAverageGrade;
    }

    public List<FeedbackHighRating> getAllFeedbackHighRating() throws Exception {
        // Get all feedback by 4-5 rating
        List<Feedback> allHighRating = feedbackRepository.findAllByRatingIn(Arrays.asList(4,5));
        if (allHighRating.isEmpty()) {
            throw new Exception("No feedback with high grades found!");
        }
        // Create empty lists
        Map<String, String> allNames = new HashMap<>();
        List<FeedbackHighRating> feedbacksWithHighRating = new ArrayList<>();
        // Loop all feedback, save names and create a new FeedbackHighGrade then save to list
        for (Feedback feedback : allHighRating) {
            if (!allNames.containsKey(feedback.getCaregiverUsername())) {
                User user = userService.findByUsername(feedback.getCaregiverUsername());
                allNames.put(feedback.getCaregiverUsername(), user.getFirstName() + " " + user.getLastName());
            }
            FeedbackHighRating highRating = new FeedbackHighRating();
            highRating.setId(feedback.getId());
            highRating.setDoctorFullName(allNames.get(feedback.getCaregiverUsername()));
            highRating.setRating(feedback.getRating());
            highRating.setComment(feedback.getComment());
            feedbacksWithHighRating.add(highRating);
        }
        // Make list in random order
        Collections.shuffle(feedbacksWithHighRating);
        // return list
        return feedbacksWithHighRating;
    }
}
