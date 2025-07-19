import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import jsPDF from 'jspdf';

export interface Prescription {
  diagnosis: string;
  medicines: string;
  instructions: string;
  notes: string;
  appointmentId: string;
  patientId: number;
  doctorId: number;
  createdDate: Date;
}
export interface Patient {
  id: number;
  name: string;
  age: number;
  gender: string;
  contactNo: string;
}

export interface AppointmentResponse {
  id: string;
  patient: Patient;
  patientId: number;
  patientName: string;
  patientAge: number;
  patientGender: string;
  patientContactNo: string;
  status: string;
  appointmentDate: string;
}

@Component({
  selector: 'app-doctor-dashboard',
  templateUrl: './doctor-dashboard.component.html',
  styleUrls: ['./doctor-dashboard.component.css']
})
export class DoctorDashboardComponent implements OnInit {
  doctor: any = {}; // Doctor details will be fetched dynamically
  appointments: any[] = []; // Appointments array
  patients: any[] = []; // Patients array
  pharmacyDetails: any[] = []; // Pharmacy details array
  selectedOption: string = ''; // Initialize selectedOption to an empty string
  showPrescriptionModal = false;
  selectedAppointment: any = null;

  prescription: Prescription = {
    diagnosis: '',
    medicines: '',
    instructions: '',
    notes: '',
    appointmentId: '',
    patientId: 0,
    doctorId: 0,
    createdDate: new Date()
  };

  showPasswordModal: boolean = false;
  showOldPassword: boolean = false;
  showNewPassword: boolean = false;
  showConfirmPassword: boolean = false;
  errorMessage: string = '';
  passwordData = {
    oldPassword: '',
    newPassword: '',
    confirmPassword:''
  };

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    // Fetch doctor details and initialize the dashboard
    this.fetchDoctorDetails();
  }

  /*openPrescriptionModal(appointment: any) {
    this.selectedAppointment = appointment;
    this.prescription.appointmentId = appointment.id;
    this.prescription.patientId = appointment.patientId;
    this.prescription.doctorId = this.doctor.id;
    this.showPrescriptionModal = true;
  }*/

  



    openPasswordModal(): void {
      this.showPasswordModal = true;
      this.errorMessage = '';
    }
    
    closePasswordModal(): void {
      this.showPasswordModal = false;
      this.errorMessage = '';
      this.passwordData = {
        oldPassword: '',
        newPassword: '',
        confirmPassword:''
      };
      this.showOldPassword = false;
      this.showNewPassword = false;
      this.showConfirmPassword = false;
    }
    
    changePassword(): void {
      const username = localStorage.getItem('username');
      if (!username) {
        this.errorMessage = 'User session not found';
        return;
      }
    
      this.http.put('http://localhost:8080/api/v1/auth/change-password', {
        username: username,
        oldPassword: this.passwordData.oldPassword,
        newPassword: this.passwordData.newPassword
      }).subscribe({
        next: () => {
          alert('Password changed successfully!');
          this.closePasswordModal();
        },
        error: (error) => {
          console.error('Error changing password:', error);
          this.errorMessage = error.error?.message || 'Failed to change password';
        }
      });
    }

    openPrescriptionModal(appointment: any): void {
      console.log('Opening modal for appointment:', appointment);
      this.selectedAppointment = appointment;
      this.prescription = {
        diagnosis: '',
        medicines: '',
        instructions: '',
        notes: '',
        appointmentId: appointment.id,
        patientId: appointment.patientId, // Make sure this exists in your appointment data
        doctorId: this.doctor.id,   
             // Make sure this exists in your doctor data
        createdDate: new Date()
      };
      this.showPrescriptionModal = true;
    }

  closePrescriptionModal() {
    this.showPrescriptionModal = false;
    this.selectedAppointment = null;
    this.resetPrescriptionForm();
  }

  resetPrescriptionForm() {
    this.prescription = {
      diagnosis: '',
      medicines: '',
      instructions: '',
      notes: '',
      appointmentId: '',
      patientId: 0,
      doctorId: 0,
      createdDate: new Date()
    };
  }

  /*savePrescription() {
    // First save to database
    this.http.post('http://localhost:8080/api/prescriptions', this.prescription)
      .subscribe({
        next: (response: any) => {
          this.generatePrescriptionPDF(response);
          this.closePrescriptionModal();
          alert('Prescription saved successfully!');
        },
        error: (error) => {
          console.error('Error saving prescription:', error);
          alert('Failed to save prescription');
        }
      });
  }*/
      savePrescription(): void {
        console.log('Saving prescription:', this.prescription);
        if (!this.prescription.patientId || !this.prescription.doctorId || !this.prescription.appointmentId) {
          alert('Missing required IDs. Please check the data.');
          return;
        }
      
        // First save to database
        this.http.post('http://localhost:8080/api/prescriptions', this.prescription)
          .subscribe({
            next: (response: any) => {
              console.log('Prescription saved:', response);
              // Generate PDF right after successful save
              // this.generatePrescriptionPDF(this.prescription); // Changed to use current prescription data
              this.closePrescriptionModal();
              alert('Prescription saved and PDF generated successfully!');
            },
            error: (error) => {
              console.error('Error saving prescription:', error);
              alert('Failed to save prescription: ' + (error.error?.message || error.message));
            }
          });
      }
      
      // generatePrescriptionPDF(prescriptionData: Prescription): void {
      //   try {
      //     console.log('Generating PDF for:', prescriptionData);
      //     const doc = new jsPDF();
          
      //     // Add hospital logo/header
      //     doc.setFontSize(20);
      //     doc.text('Hospital Management System', 105, 20, { align: 'center' });
          
      //     // Add prescription details with current date and time
      //     doc.setFontSize(12);
      //     doc.text(`Date: ${new Date().toLocaleString()}`, 20, 40);
      //     doc.text(`Doctor: Dr. ${this.doctor.name}`, 20, 50);
      //     doc.text(`Specialization: ${this.doctor.specialist}`, 20, 60);
          
      //     doc.text(`Patient: ${this.selectedAppointment.patientName}`, 20, 80);
      //     doc.text(`Age/Gender: ${this.selectedAppointment.patientAge}/${this.selectedAppointment.patientGender}`, 20, 90);
          
      //     // Add prescription content
      //     doc.text('Diagnosis:', 20, 110);
      //     doc.text(prescriptionData.diagnosis, 30, 120);
          
      //     doc.text('Medicines:', 20, 140);
      //     doc.text(prescriptionData.medicines, 30, 150);
          
      //     doc.text('Instructions:', 20, 170);
      //     doc.text(prescriptionData.instructions, 30, 180);
          
      //     if (prescriptionData.notes) {
      //       doc.text('Additional Notes:', 20, 200);
      //       doc.text(prescriptionData.notes, 30, 210);
      //     }
          
      //     // Add footer with signature
      //     doc.text('Signature: _________________', 150, 250);
          
      //     // Force download the PDF
      //     const pdfOutput = doc.output('blob');
      //     const url = window.URL.createObjectURL(pdfOutput);
      //     const link = document.createElement('a');
      //     link.href = url;
      //     link.download = `prescription_${this.selectedAppointment.id}.pdf`;
      //     link.click();
      //     window.URL.revokeObjectURL(url);
      
      //     console.log('PDF generated and download initiated');
      //   } catch (error) {
      //     console.error('Error generating PDF:', error);
      //     alert('Error generating PDF. Please try again.');
      //   }
      // }
      
  fetchDoctorDetails(): void {
    const username = localStorage.getItem('username'); // Fetch username from localStorage
    if (!username) {
      console.error('Username not found in localStorage');
      alert('Please log in again.');
      return;
    }
  
    this.http.get(`http://localhost:8080/api/v1/doctors-info/username/${username}`).subscribe(
      (data: any) => {
        console.log('Doctor details fetched:', data); // Debugging log
        this.doctor = data; // Assign the fetched data to the doctor variable
      },
      (error) => {
        console.error('Error fetching doctor details:', error);
        alert('Failed to fetch doctor details. Please try again later.');
      }
    );
  }

  fetchAppointments(): void {
    const username = localStorage.getItem('username');
    if (!username) {
      console.error('Username not found in localStorage');
      alert('Please log in again.');
      return;
    }
  
    this.selectedOption = 'appointments';
    this.http.get<AppointmentResponse[]>(`http://localhost:8080/api/appointments/doctor/username/${username}`).subscribe({
      next: (data: AppointmentResponse[]) => {
        console.log('Appointments fetched:', data);
        this.appointments = data;
      },
      error: (error) => {
        console.error('Error fetching appointments:', error);
        alert('Failed to fetch appointments. Please try again later.');
      }
    });
  }
  /*viewPatients(): void {
    this.selectedOption = 'patients'; // Set selectedOption to 'patients'
    this.http.get('http://localhost:8080/api/v1/patients-info').subscribe(
      (data: any) => {
        this.patients = data;
      },
      (error) => {
        console.error('Error fetching patients:', error);
      }
    );
  }*/

    viewPatients(): void {
      this.selectedOption = 'patients';
      const username = localStorage.getItem('username');
      if (!username) {
        console.error('Username not found in localStorage');
        alert('Please log in again.');
        return;
      }
    
      // Get patients for the specific doctor
      this.http.get(`http://localhost:8080/api/v1/doctors-info/${this.doctor.id}/patients`).subscribe({
        next: (data: any) => {
          console.log('Patients fetched:', data);
          this.patients = data;
        },
        error: (error) => {
          console.error('Error fetching patients:', error);
          alert('Failed to fetch patients. Please try again later.');
        }
      });
    }


  fetchPharmacyDetails(): void {
    this.selectedOption = 'pharmacy'; // Set selectedOption to 'pharmacy'
    this.http.get('http://localhost:8082/api/pharmacy/medicines').subscribe(
      (data: any) => {
        this.pharmacyDetails = data;
      },
      (error) => {
        console.error('Error fetching pharmacy details:', error);
      }
    );
  }
  acceptAppointment(appointmentId: number): void {
    const statusUpdate = { appointmentId: appointmentId, status: 'Accepted' };
    this.http.put('http://localhost:8080/api/appointments/status', statusUpdate).subscribe({
      next: () => {
        alert('Appointment accepted successfully!');
        const appointment = this.appointments.find(a => a.id === appointmentId);
        if (appointment) {
          appointment.status = 'Accepted';
        }
      },
      error: (err) => {
        console.error('Error accepting appointment:', err);
        alert('Failed to accept the appointment. Please try again.');
      }
    });
  }
  
  declineAppointment(appointmentId: number): void {
    const statusUpdate = { appointmentId: appointmentId, status: 'Declined' };
    this.http.put('http://localhost:8080/api/appointments/status', statusUpdate).subscribe({
      next: () => {
        alert('Appointment declined successfully!');
        const appointment = this.appointments.find(a => a.id === appointmentId);
        if (appointment) {
          appointment.status = 'Declined';
        }
      },
      error: (err) => {
        console.error('Error declining appointment:', err);
        alert('Failed to decline the appointment. Please try again.');
      }
    });
  }
  logout(): void {
    this.http.post('http://localhost:8080/api/v1/auth/logout', {}).subscribe({
      next: () => {
        localStorage.clear(); // Clear local storage
        window.location.href = '/hms-main'; // Redirect to login page
      },
      error: (err) => {
        console.error('Error during logout:', err);
        alert('Failed to log out. Please try again.');
      }
    });
  }

}