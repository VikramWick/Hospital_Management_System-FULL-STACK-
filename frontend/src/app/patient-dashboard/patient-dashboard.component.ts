import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import jsPDF from 'jspdf';

@Component({
  selector: 'app-patient-dashboard',
  templateUrl: './patient-dashboard.component.html',
  styleUrls: ['./patient-dashboard.component.css']
})


export class PatientDashboardComponent implements OnInit {
  patient: any = {}; // Logged-in patient details
  doctors: any[] = []; // List of all doctors
  appointments: any[] = [];
  insurancePlans: any[] = []; // Insurance plans list
  doctorSearchTerm: string = '';
  filteredDoctors: any[] = [];
  prescriptions: any[] = [];
  showInsurancePlans: boolean = false;

  errorMessage: string = '';
  showPasswordModal: boolean = false;
  showOldPassword: boolean = false;
  showNewPassword: boolean = false;
  showConfirmPassword: boolean = false;
  passwordData = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  };

  showAppointmentModal: boolean = false;
  selectedDoctor: any = null;
  appointmentError: string = '';
  minDate: string = new Date().toISOString().split('.')[0].slice(0, -3);
  appointmentRequest = {
    preferredDate: '',
    notes: ''
  };


  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchPatientDetails();
    this.fetchDoctors();
    this.fetchAppointments();
  }

   requestAppointment(doctor: any): void {
    this.selectedDoctor = doctor;
    this.showAppointmentModal = true;
    this.appointmentError = '';
    this.appointmentRequest = {
      preferredDate: '',
      notes: ''
    };
  }

  navigateToPharmacyPurchase(): void {
  this.router.navigate(['/pharmacy-purchase']);
}

purchaseInsurance(plan: any): void {
  this.router.navigate(['/insurance-purchase']);
}

  closeAppointmentModal(): void {
    this.showAppointmentModal = false;
    this.selectedDoctor = null;
    this.appointmentError = '';
  }

  isValidAppointmentRequest(): boolean {
    return this.appointmentRequest.preferredDate !== '';
  }

  submitAppointmentRequest(): void {
    if (!this.isValidAppointmentRequest()) {
        this.appointmentError = 'Please select a preferred date and time';
        return;
    }

    const request = {
        patientId: this.patient.id,
        doctorId: this.selectedDoctor.id,
        preferredDate: this.appointmentRequest.preferredDate,
        notes: this.appointmentRequest.notes || '' // Add default empty string if notes is null
    };

    this.http.post('http://localhost:8080/api/v1/appointment-requests', request).subscribe({
        next: () => {
            alert('Appointment request submitted successfully! Admin will review and confirm.');
            this.closeAppointmentModal();
        },
        error: (error) => {
            console.error('Error submitting appointment request:', error);
            this.appointmentError = 'Failed to submit appointment request. Please try again.';
        }
    });
}

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
      confirmPassword: ''
    };
    this.showOldPassword = false;
    this.showNewPassword = false;
    this.showConfirmPassword = false;
  }
  
  isValidPassword(): boolean {
    return (
      this.passwordData.oldPassword.length > 0 &&
      this.passwordData.newPassword.length > 0 &&
      this.passwordData.newPassword === this.passwordData.confirmPassword
    );
  }
  
  changePassword(): void {
    this.errorMessage = '';
    
    if (!this.isValidPassword()) {
      this.errorMessage = 'Please ensure all password requirements are met.';
      return;
    }
  
    const username = localStorage.getItem('username');
    if (!username) {
      this.errorMessage = 'Session expired. Please log in again.';
      return;
    }
  
    this.http.put('http://localhost:8080/api/v1/auth/change-password', {
      username: username,
      oldPassword: this.passwordData.oldPassword,
      newPassword: this.passwordData.newPassword
    }).subscribe({
      next: (response: any) => {
        alert('Password changed successfully!');
        this.closePasswordModal();
      },
      error: (error) => {
        console.error('Error changing password:', error);
        this.errorMessage = error.error?.message || 'Failed to change password. Please check your current password.';
      }
    });
  }

  

  filterDoctors(): void {
    const searchTerm = this.doctorSearchTerm.toLowerCase();
    this.filteredDoctors = this.doctors.filter(doctor => 
      doctor.name.toLowerCase().includes(searchTerm) ||
      doctor.specialist.toLowerCase().includes(searchTerm)
    );
  }

  fetchPatientDetails(): void {
    const username = localStorage.getItem('username'); // Fetch username from localStorage
    if (!username) {
      console.error('Username not found in localStorage');
      alert('Please log in again.');
      return;
    }

    this.http.get(`http://localhost:8080/api/v1/patients-info/username/${username}`).subscribe(
      (data: any) => {
        this.patient = data;
        if (this.patient && this.patient.id) {
          console.log('Fetching prescriptions for patient ID:', this.patient.id);
          this.fetchPrescriptions();
        } else {
          console.error('Patient ID is missing from loaded data:', data);
        }
      },
      (error) => {
        console.error('Error fetching patient details:', error);
        alert('Failed to fetch patient details. Please try again later.');
      }
    );
  
    
  }

  fetchDoctors(): void {
    this.http.get<any[]>('http://localhost:8080/api/v1/doctors-info').subscribe({
      next: (doctors) => {
        this.doctors = doctors;
        this.filteredDoctors = doctors; // Initialize filtered list
      },
      error: (error) => {
        console.error('Error fetching doctors:', error);
      }
    });
  }

  fetchAppointments(): void {
    const username = localStorage.getItem('username'); // Fetch username dynamically
    if (!username) {
      console.error('Username not found in localStorage');
      alert('Please log in again.');
      return;
    }

    this.http.get(`http://localhost:8080/api/appointments/patient/username/${username}`).subscribe(
      (data: any) => {
        this.appointments = data;
      },
      (error) => {
        console.error('Error fetching appointments:', error);
      }
    );
  }

  fetchInsurancePlans(): void {
    this.http.get('http://localhost:8083/api/insurance/plans').subscribe({
      next: (response: any) => {
        this.insurancePlans = response; // Assign the fetched insurance plans
        // alert('Insurance plans fetched successfully!');
      },
      error: (err) => {
        console.error('Error fetching insurance plans:', err);
        this.insurancePlans = []; // Clear the insurance plans array
        alert('Failed to fetch insurance plans. Please try again later.');
      }
    });
  }

  
  
  fetchPrescriptions(): void {
    const username = localStorage.getItem('username');
    if (!username) {
      console.error('Username not found in localStorage');
      return;
    }

    this.http.get(`http://localhost:8080/api/prescriptions/patient/${this.patient.id}`)
      .subscribe({
        next: (data: any) => {
          this.prescriptions = data;
        },
        error: (error) => {
          console.error('Error fetching prescriptions:', error);
        }
      });
  }

 downloadPrescription(prescription: any): void {
  try {
    const doc = new jsPDF();
    
    // Add watermark
    doc.setTextColor(230, 230, 230);  // Light gray
    doc.setFontSize(60);
    doc.setFont('helvetica', 'bold');
    doc.text('MEDORA HEALTH', 105, 140, {
      align: 'center',
      angle: 45
    });

    // Reset text color and font for content
    doc.setTextColor(0, 0, 0);
    doc.setFont('helvetica', 'normal');

    // Add hospital header
    doc.setFontSize(24);
    doc.setFont('helvetica', 'bold');
    doc.setTextColor(41, 128, 185); // Professional blue
    doc.text('MEDORA HEALTH', 105, 25, { align: 'center' });
    
    // Add contact info
    doc.setFontSize(10);
    doc.setTextColor(100, 100, 100); // Gray
    doc.text('123 Healthcare Avenue, Medical District', 105, 32, { align: 'center' });
    doc.text('Phone: +91 1234567890 | Email: care@medorahealth.com', 105, 37, { align: 'center' });
    
    // Add horizontal line
    doc.setDrawColor(41, 128, 185);
    doc.setLineWidth(0.5);
    doc.line(20, 42, 190, 42);

    // Prescription details in a professional layout
    doc.setFontSize(12);
    doc.setTextColor(0, 0, 0);
    doc.setFont('helvetica', 'normal');

    // Create two columns
    const leftColumn = 25;
    const rightColumn = 115;
    let currentY = 60;

    // Patient and Doctor details in columns
    doc.setFont('helvetica', 'bold');
    doc.text('Patient Details', leftColumn, currentY);
    doc.text('Doctor Details', rightColumn, currentY);
    
    currentY += 8;
    doc.setFont('helvetica', 'normal');
    doc.text(`Name: ${this.patient.name}`, leftColumn, currentY);
    doc.text(`Dr. ${prescription.doctorName}`, rightColumn, currentY);
    
    currentY += 7;
    doc.text(`Age/Gender: ${this.patient.age}/${this.patient.gender}`, leftColumn, currentY);
    doc.text(`Specialist`, rightColumn, currentY);
    
    currentY += 7;
    doc.text(`Contact: ${this.patient.contactNo}`, leftColumn, currentY);
    

    // Add date and prescription ID
    currentY += 15;
    doc.setFont('helvetica', 'bold');
    doc.text(`Date: ${new Date(prescription.createdDate).toLocaleDateString()}`, leftColumn, currentY);
    doc.text(`Prescription ID: ${prescription.id}`, rightColumn, currentY);

    // Add horizontal line
    currentY += 10;
    doc.line(20, currentY, 190, currentY);

    // Diagnosis section
    currentY += 15;
    doc.setFont('helvetica', 'bold');
    doc.text('Diagnosis:', leftColumn, currentY);
    currentY += 7;
    doc.setFont('helvetica', 'normal');
    doc.text(prescription.diagnosis, leftColumn + 5, currentY);

    // Medicines section
    currentY += 15;
    doc.setFont('helvetica', 'bold');
    doc.text('Medicines:', leftColumn, currentY);
    currentY += 7;
    doc.setFont('helvetica', 'normal');
    const medicines = prescription.medicines.split(',');
    medicines.forEach((medicine: string) => {
      doc.text(`• ${medicine.trim()}`, leftColumn + 5, currentY);
      currentY += 7;
    });

    // Instructions section
    currentY += 8;
    doc.setFont('helvetica', 'bold');
    doc.text('Instructions:', leftColumn, currentY);
    currentY += 7;
    doc.setFont('helvetica', 'normal');
    doc.text(prescription.instructions, leftColumn + 5, currentY);

    // Add notes if present
    if (prescription.notes) {
      currentY += 15;
      doc.setFont('helvetica', 'bold');
      doc.text('Additional Notes:', leftColumn, currentY);
      currentY += 7;
      doc.setFont('helvetica', 'normal');
      doc.text(prescription.notes, leftColumn + 5, currentY);
    }

    // Add footer
    doc.setDrawColor(41, 128, 185);
    doc.line(20, 260, 190, 260);
    
    // Digital Signature placeholder
    doc.setFont('helvetica', 'bold');
    doc.text('Digital Signature', 150, 270);
    doc.setFont('helvetica', 'normal');
    doc.text('This is a digitally generated prescription', 105, 280, { align: 'center' });

    // Save the PDF
    doc.save(`prescription_${prescription.id}.pdf`);
    
  } catch (error) {
    console.error('Error generating PDF:', error);
    alert('Error generating PDF. Please try again.');
  }
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