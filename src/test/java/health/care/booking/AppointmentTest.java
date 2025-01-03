package health.care.booking;

import health.care.booking.models.Appointment;
import health.care.booking.models.User;
import health.care.booking.models.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {

    private Appointment appointment;
    private User patient;
    private User caregiver;

    @BeforeEach
    void setUp() {
        // Initialize the User objects
        patient = new User();
        patient.setId("patient1");
        patient.setFirstName("John");
        patient.setLastName("Doe");

        caregiver = new User();
        caregiver.setId("caregiver1");
        caregiver.setFirstName("Dr. Smith");

        // Create an Appointment object
        appointment = new Appointment();
        appointment.setId("1");
        appointment.setPatientId(patient);
        appointment.setCaregiverId(caregiver);
        appointment.setStatus(Status.SCHEDULED);
    }

    @Test
    void testAppointmentCreation() {
        assertNotNull(appointment);
        assertEquals("1", appointment.getId());
        assertEquals(patient, appointment.getPatientId());
        assertEquals(caregiver, appointment.getCaregiverId());
        assertEquals(LocalDateTime.of(2025, 1, 2, 10, 30), appointment.getDateTime());
        assertEquals(Status.SCHEDULED, appointment.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        appointment.setId("2");
        appointment.setStatus(Status.COMPLETED);

        assertEquals("2", appointment.getId());
        assertEquals(LocalDateTime.of(2025, 2, 3, 14, 0), appointment.getDateTime());
        assertEquals(Status.COMPLETED, appointment.getStatus());
    }

    @Test
    void testAppointmentStatus() {
        appointment.setStatus(Status.CANCELLED);
        assertEquals(Status.CANCELLED, appointment.getStatus());
        appointment.setStatus(Status.COMPLETED);
        assertEquals(Status.COMPLETED, appointment.getStatus());
    }
}
