import { Component, NgZone, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface NewBill {
  patientId: number | null;
  doctorId: number | null;
  appointmentId: string;
  amount: number | null;
  description: string;
  status: string;
}

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit {
  doctors: any[] = [];
  patients: any[] = [];
  bills: any[] = [];
  appointments: any[] = [];
  selectedOption: string = '';
  requests: any[] = [];
  filteredRequests: any[] = [];
  requestSearchTerm: string = '';
  
  // selectedOption: 'patients' | 'doctors' | 'appointments' | 'bills' | 'createPatient' | 'editPatient' | 'createDoctor' | 'editDoctor' | 'createBill' | 'createAppointment' = 'patients';
  activeMenu: string | null = null;
  doctorSearchTerm: string = '';
  patientSearchTerm: string = '';
  filteredDoctors: any[] = [];
  filteredPatients: any[] = [];
  appointmentSearchTerm: string = ''; 
  filteredAppointments: any[] = []; 
  billSearchTerm: string = ''; 
  filteredBills: any[] = []; 
  editDoctor: any = {}; // Holds the doctor details being edited
  editPatient: any = {}; // Holds the patient details being edited

  newDoctor = { username: '', password: '', name: '', specialist: '', age: null, gender: '' };
  newPatient = { username: '', password: '', name: '', age: null, gender: '', contactNo: '' };
  newAppointment = { patientId: null, doctorId: null, appointmentDate: '' };

  newBill: NewBill = {
    patientId: null,
    doctorId: null,
    appointmentId: '',
    amount: null,
    description: '',
    status: 'PENDING'
};

  constructor(private http: HttpClient, private zone: NgZone) {}

  ngOnInit(): void {
    this.fetchDoctors();
    this.fetchPatients();
    this.fetchBills();
    this.fetchAppointments();
    this.fetchBookingRequests();
  }

  toggleMenu(section: string): void {
    this.activeMenu = this.activeMenu === section ? null : section;
  }

  viewBookingRequests(): void {
    this.selectedOption = 'bookingRequests';
    this.activeMenu = '';
    this.fetchBookingRequests();
  }
  
  fetchBookingRequests(): void {
    this.http.get('http://localhost:8080/api/v1/appointment-requests').subscribe({
      next: (response: any) => {
        this.requests = response;
        this.filteredRequests = response;
      },
      error: (error) => {
        console.error('Error fetching booking requests:', error);
      }
    });
  }
  
  filterRequests(): void {
    if (!this.requestSearchTerm) {
      this.filteredRequests = this.requests;
      return;
    }
  
    const searchTerm = this.requestSearchTerm.toLowerCase();
    this.filteredRequests = this.requests.filter(request => 
      request.patient?.name?.toLowerCase().includes(searchTerm) ||
      request.doctor?.name?.toLowerCase().includes(searchTerm)
    );
  }
  
  createAppointmentFromRequest(request: any): void {
    const appointmentData = {
      patientId: request.patient.id,
      doctorId: request.doctor.id,
      appointmentDate: request.preferredDate,
      status: 'Scheduled'
    };
  
    this.http.post('http://localhost:8080/api/v1/admin/createappointment', appointmentData).subscribe({
      next: () => {
        this.deleteRequest(request.id);
        alert('Appointment created successfully!');
        this.fetchAppointments();
      },
      error: (error) => {
        console.error('Error creating appointment:', error);
        alert('Failed to create appointment');
      }
    });
  }
  
  deleteRequest(requestId: number): void {
    if (confirm('Are you sure you want to delete this request?')) {
      this.http.delete(`http://localhost:8080/api/v1/appointment-requests/${requestId}`).subscribe({
        next: () => {
          this.fetchBookingRequests();
        },
        error: (error) => {
          console.error('Error deleting request:', error);
          alert('Failed to delete request');
        }
      });
    }
  }

  openCreateDoctorForm(): void {
    this.selectedOption = 'createDoctor';
  }

  openCreatePatientForm(): void {
    this.selectedOption = 'createPatient';
  }

  openCreateAppointmentForm(): void {
    this.selectedOption = 'createAppointment';
  }

  openCreateBillForm(): void {
    this.selectedOption = 'createBill';
  }

  viewDoctors(): void {
    console.log('View Doctors button clicked');
    this.selectedOption = 'doctors';
  }

  viewPatients(): void {
    this.selectedOption = 'patients';
  }

  viewBills(): void {
    this.selectedOption = 'bills';
  }

  viewAppointments(): void {
    this.selectedOption = 'appointments';
  }
  openEditDoctorForm(doctor: any): void {
    this.editDoctor = { ...doctor }; // Clone the doctor object to avoid direct mutation
    this.selectedOption = 'editDoctor'; // Switch to the edit form
  }
  
  openEditPatientForm(patient: any): void {
    this.editPatient = { ...patient }; // Clone the patient object to avoid direct mutation
    this.selectedOption = 'editPatient'; // Switch to the edit form
  }
  filterDoctors(): void {
    this.filteredDoctors = this.doctors.filter((doctor) =>
      doctor.name.toLowerCase().includes(this.doctorSearchTerm.toLowerCase()) ||
      doctor.specialist.toLowerCase().includes(this.doctorSearchTerm.toLowerCase())
    );
  }
  
  filterPatients(): void {
    this.filteredPatients = this.patients.filter((patient) =>
      patient.name.toLowerCase().includes(this.patientSearchTerm.toLowerCase()) ||
      patient.username.toLowerCase().includes(this.patientSearchTerm.toLowerCase())
    );
  }

  filterAppointments(): void {
    const searchTerm = this.appointmentSearchTerm.toLowerCase();
    this.filteredAppointments = this.appointments.filter((appointment) => {
      return (
       
        appointment.id?.toString().toLowerCase().includes(searchTerm) ||
      
        appointment.patient?.name?.toLowerCase().includes(searchTerm) ||
    
        appointment.doctor?.name?.toLowerCase().includes(searchTerm) ||
    
        appointment.doctor?.id?.toString().toLowerCase().includes(searchTerm) ||
        appointment.patient?.id?.toString().toLowerCase().includes(searchTerm)
      );
    });
  }

  filterBills(): void {
    const searchTerm = this.billSearchTerm.toLowerCase();
    this.filteredBills = this.bills.filter((bill) =>
      bill.patient.name.toLowerCase().includes(searchTerm) ||
      bill.appointment.id.toString().includes(searchTerm)
    );
  }

  fetchDoctors(): void {
    this.http.get('http://localhost:8080/api/v1/doctors-info').subscribe({
      next: (response: any) => {
        this.doctors = response;
        this.filteredDoctors = response; // Initialize filteredDoctors
      },
      error: (err) => {
        console.error('Error fetching doctors:', err);
      }
    });
  }
  
  fetchPatients(): void {
    this.http.get('http://localhost:8080/api/v1/patients-info').subscribe({
      next: (response: any) => {
        this.patients = response;
        this.filteredPatients = response; // Initialize filteredPatients
      },
      error: (err) => {
        console.error('Error fetching patients:', err);
      }
    });
  }

  fetchBills(): void {
    this.http.get('http://localhost:8080/api/v1/admin/bills').subscribe({
      next: (response: any) => {
        this.bills = response;
        this.filteredBills = response; 
      },
      error: (err) => {
        console.error('Error fetching bills:', err);
      }
    });
  }

  fetchAppointments(): void {
    this.http.get('http://localhost:8080/api/appointments').subscribe({
      next: (response: any) => {
        this.appointments = response;
        this.filteredAppointments = response; 
      },
      error: (err) => {
        console.error('Error fetching appointments:', err);
      }
    });
  }

  createDoctor(): void {
    this.http.post('http://localhost:8080/api/v1/admin/doctors-info', this.newDoctor).subscribe({
      next: () => {
        alert('Doctor created successfully!');
        this.newDoctor = { username: '', password: '', name: '', specialist: '', age: null, gender: '' };
        this.fetchDoctors();
      },
      error: (err) => {
        console.error('Error creating doctor:', err);
        alert('Failed to create doctor.');
      }
    });
  }

  createPatient(): void {
    this.http.post('http://localhost:8080/api/v1/admin/patients-info', this.newPatient).subscribe({
      next: () => {
        alert('Patient created successfully!');
        this.newPatient = { username: '', password: '', name: '', age: null, gender: '', contactNo: '' };
        this.fetchPatients();
      },
      error: (err) => {
        console.error('Error creating patient:', err);
        alert('Failed to create patient.');
      }
    });
  }

  createAppointment(): void {
    // Convert the appointmentDate to the correct LocalDateTime format
    const formattedDate = this.newAppointment.appointmentDate
      ? `${this.newAppointment.appointmentDate}:00` // Add seconds to match LocalDateTime format
      : '';
  
    const appointmentPayload = {
      patientId: this.newAppointment.patientId,
      doctorId: this.newAppointment.doctorId,
      appointmentDate: formattedDate, // Use the formatted date
      status: 'Scheduled' // Default status
    };
  
    this.http.post('http://localhost:8080/api/v1/admin/createappointment', appointmentPayload).subscribe({
      next: () => {
        alert('Appointment created successfully!');
        this.newAppointment = { patientId: null, doctorId: null, appointmentDate: '' };
        this.fetchAppointments();
      },
      error: (err) => {
        console.error('Error creating appointment:', err);
        alert('Failed to create appointment.');
      }
    });
  }

 
    createBill(): void {
      const billPayload = {
          ...this.newBill,
          appointmentId: this.newBill.appointmentId.trim(),
          status: 'PENDING'
      };
  
      // Using patientId in the URL path
      this.http.post(
          `http://localhost:8080/api/v1/admin/createbill/${billPayload.patientId}`, 
          billPayload
      ).subscribe({
          next: () => {
              alert('Bill created successfully!');
              this.newBill = {
                  patientId: null,
                  doctorId: null,
                  appointmentId: '',
                  amount: null,
                  description: '',
                  status: 'PENDING'
              };
              this.fetchBills();
              this.selectedOption = 'bills';
          },
          error: (err) => {
              console.error('Error creating bill:', err);
              if (err.status === 400) {
                  alert(err.error.message || 'Invalid input data');
              } else {
                  alert('Failed to create bill. Please try again.');
              }
          }
      });
  }

    
  confirmDeleteDoctor(doctorId: number): void {
    if (confirm('Are you sure you want to delete this doctor?')) {
      this.http.delete(`http://localhost:8080/api/v1/admin/doctors-info/${doctorId}`).subscribe({
        next: () => {
          alert('Doctor deleted successfully.');
          this.fetchDoctors();
        },
        error: (err) => {
          console.error('Error deleting doctor:', err);
          alert('Failed to delete the doctor.');
        }
      });
    }
  }

  confirmDeletePatient(patientId: number): void {
    if (confirm('Are you sure you want to delete this patient?')) {
      this.http.delete(`http://localhost:8080/api/v1/admin/patients-info/${patientId}`).subscribe({
        next: () => {
          alert('Patient deleted successfully.');
          this.fetchPatients();
        },
        error: (err) => {
          console.error('Error deleting patient:', err);
          alert('Failed to delete the patient.');
        }
      });
    }
  }


  confirmDeleteAppointment(appointmentId: number): void {
    if (confirm('Are you sure you want to delete this appointment?')) {
      this.http.delete(`http://localhost:8080/api/appointments/${appointmentId}`).subscribe({
        next: () => {
          alert('Appointment deleted successfully.');
          this.fetchAppointments(); // Refresh the list of appointments
        },
        error: (err) => {
          console.error('Error deleting appointment:', err);
          alert('Failed to delete the appointment.');
        }
      });
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

  
  confirmDeleteBill(billId: number): void {
    if (confirm('Are you sure you want to delete this bill?')) {
      this.http.delete(`http://localhost:8080/api/billings/${billId}`).subscribe({
        next: () => {
          alert('Bill deleted successfully.');
          this.fetchBills(); // Refresh the list of bills
        },
        error: (err) => {
          console.error('Error deleting bill:', err);
          alert('Failed to delete the bill.');
        }
      });
    }
  }
  updateDoctor(): void {
    this.http.put(`http://localhost:8080/api/v1/admin/doctors-info/${this.editDoctor.id}`, this.editDoctor).subscribe({
      next: () => {
        alert('Doctor updated successfully!');
        this.fetchDoctors(); // Refresh the doctor list
        this.selectedOption = 'doctors'; // Switch back to the doctor list
      },
      error: (err) => {
        console.error('Error updating doctor:', err);
        alert('Failed to update doctor.');
      }
    });
  }
  updatePatient(): void {
    this.http.put(`http://localhost:8080/api/v1/admin/patients-info/${this.editPatient.id}`, this.editPatient).subscribe({
      next: () => {
        alert('Patient updated successfully!');
        this.fetchPatients(); // Refresh the patient list
        this.selectedOption = 'patients'; // Switch back to the patient list
      },
      error: (err) => {
        console.error('Error updating patient:', err);
        alert('Failed to update patient.');
      }
    });
  }

  
  
}