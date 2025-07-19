package com.hospital.hms.controller;
import com.hospital.hms.dto.AppointmentDTO;
import com.hospital.hms.dto.BillingDTO;
import com.hospital.hms.model.Appointment;
import com.hospital.hms.model.Billing;
import com.hospital.hms.model.Patient;
import com.hospital.hms.model.User;
import com.hospital.hms.model.User.Role;
import com.hospital.hms.model.Doctor;
import com.hospital.hms.service.BillingService;
import com.hospital.hms.repository.PatientRepository;
import com.hospital.hms.repository.UserRepository;
import com.hospital.hms.repository.AppointmentRepository;
import com.hospital.hms.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/v1/admin")
public class AdminController {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private BillingService billingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Create a patient
    @PostMapping("/patients-info")
    public ResponseEntity<Map<String, String>> createPatient(@RequestBody Patient patient) {
        try {
            // Extract password from the Patient object
            String password = patient.getPassword();

            // Save patient credentials in the User table
            User user = new User(patient.getUsername(), password, Role.Patient);
            userRepository.save(user);

            // Save patient details in the Patient table
            patientRepository.save(patient);

            // Return a JSON response
            return ResponseEntity.ok(Map.of("message", "Patient created successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while creating the patient."));
        }
    }

    // Create a doctor
    @PostMapping("/doctors-info")
    public ResponseEntity<Map<String, String>> createDoctor(@RequestBody Doctor doctor) {
        try {
            // Validate input
            if (doctor.getUsername() == null || doctor.getPassword() == null || doctor.getName() == null ||
                    doctor.getSpecialist() == null || doctor.getAge() == 0 || doctor.getGender() == null) {
                return ResponseEntity.badRequest().body(Map.of("message",
                        "All fields (username, password, name, specialist, age, gender) are required."));
            }

            // Check if username already exists
            if (userRepository.findByUsername(doctor.getUsername()).isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Username already exists."));
            }

            // Save doctor credentials in the User table
            User user = new User(doctor.getUsername(), doctor.getPassword(), Role.Doctor);
            userRepository.save(user);

            // Save doctor details in the Doctor table
            doctorRepository.save(doctor);
            
            // Return a JSON response
            return ResponseEntity.ok(Map.of("message", "Doctor created successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while creating the doctor."));
        }
    }

    // Create a bill for a patient
    @PostMapping("/createbill/{patientId}")
    public ResponseEntity<Map<String, String>> createBill(@PathVariable Long patientId,
            @RequestBody BillingDTO billingDTO) {
        try {
            // Fetch patient by ID
            Optional<Patient> patientOptional = patientRepository.findById(patientId);
            if (!patientOptional.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Patient not found"));
            }

            // Fetch doctor by ID
            Optional<Doctor> doctorOptional = doctorRepository.findById(billingDTO.getDoctorId());
            if (!doctorOptional.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Doctor not found"));
            }

            // Fetch appointment by ID
            Optional<Appointment> appointmentOptional = appointmentRepository
                    .findById(String.valueOf(billingDTO.getAppointmentId()));
            if (!appointmentOptional.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Appointment not found"));
            }

            // Create Billing entity
            Patient patient = patientOptional.get();
            Doctor doctor = doctorOptional.get();
            Appointment appointment = appointmentOptional.get();

            Billing billing = new Billing();
            billing.setPatient(patient);
            billing.setDoctor(doctor);
            billing.setAppointment(appointment); // Associate the appointment
            billing.setAmount(billingDTO.getAmount());
            billing.setDescription(billingDTO.getDescription());
            billing.setStatus(billingDTO.getStatus());
            billing.setBillingDate(billingDTO.getBillingDate());

            // Save billing entity
            // Convert Billing to BillingDTO
            BillingDTO billingDTOToSave = new BillingDTO();
            billingDTOToSave.setPatientId(patient.getId());
            billingDTOToSave.setDoctorId(doctor.getId());
            billingDTOToSave.setAppointmentId(appointment.getId());
            billingDTOToSave.setAmount(billing.getAmount());
            billingDTOToSave.setDescription(billing.getDescription());
            billingDTOToSave.setStatus(billing.getStatus());
            billingDTOToSave.setBillingDate(billing.getBillingDate());

            billingService.createBill(billingDTOToSave);

            // Return a JSON response
            return ResponseEntity.ok(Map.of("message", "Bill created successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while creating the bill."));
        }
    }

    // Delete a doctor by ID
    @DeleteMapping("/doctors-info/{id}")
    public ResponseEntity<Map<String, String>> deleteDoctor(@PathVariable Long id) {
        try {
            // Fetch the doctor by ID
            Optional<Doctor> doctorOptional = doctorRepository.findById(id);
            if (!doctorOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Doctor not found"));
            }

            // Fetch the associated user by username
            Doctor doctor = doctorOptional.get();
            Optional<User> userOptional = userRepository.findByUsername(doctor.getUsername());
            if (userOptional.isPresent()) {
                userRepository.delete(userOptional.get()); // Delete the user record
            }

            // Delete the doctor record
            doctorRepository.deleteById(id);

            return ResponseEntity.ok(Map.of("message", "Doctor deleted successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while deleting the doctor."));
        }
    }

    // Delete a patient by ID
    @DeleteMapping("/patients-info/{id}")
    public ResponseEntity<Map<String, String>> deletePatient(@PathVariable Long id) {
        try {
            // Fetch the patient by ID
            Optional<Patient> patientOptional = patientRepository.findById(id);
            if (!patientOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Patient not found"));
            }

            // Fetch the associated user by username
            Patient patient = patientOptional.get();
            Optional<User> userOptional = userRepository.findByUsername(patient.getUsername());
            if (userOptional.isPresent()) {
                userRepository.delete(userOptional.get()); // Delete the user record
            }

            // Delete the patient record
            patientRepository.deleteById(id);

            return ResponseEntity.ok(Map.of("message", "Patient deleted successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while deleting the patient."));
        }
    }

    @GetMapping("/bills")
    public ResponseEntity<List<Billing>> getAllBills() {
        return ResponseEntity.ok(billingService.getAllBills());
    }

    @PostMapping("/createappointment")
    public ResponseEntity<Map<String, String>> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        try {
            // Fetch patient by ID
            Optional<Patient> patientOptional = patientRepository.findById(appointmentDTO.getPatientId());
            if (!patientOptional.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Patient not found"));
            }

            // Fetch doctor by ID
            Optional<Doctor> doctorOptional = doctorRepository.findById(appointmentDTO.getDoctorId());
            if (!doctorOptional.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Doctor not found"));
            }

            // Create Appointment entity
            Patient patient = patientOptional.get();
            Doctor doctor = doctorOptional.get();

            Appointment appointment = new Appointment();
            appointment.setPatient(patient);
            appointment.setDoctor(doctor);
            appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());
            appointment.setStatus("Scheduled");

            // Save appointment
            appointmentRepository.save(appointment);

            return ResponseEntity.ok(Map.of("message", "Appointment created successfully!"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while creating the appointment."));
        }
    }

}