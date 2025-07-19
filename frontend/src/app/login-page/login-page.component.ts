import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string = '';
  showIdField: boolean = false; 
  showChangePassword: boolean = false;
  changePasswordData = {
    username: '',
    oldPassword: '',
    newPassword: ''
  };

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      role: ['', Validators.required],
      id: [''] // ID field is optional initially
    });

    // Listen for role changes to toggle the ID field
    this.loginForm.get('role')?.valueChanges.subscribe((value) => {
    });
  }

  onRoleChange(event: any): void {
    const selectedRole = event.target.value;
  }

 
  onSubmit(): void {
    if (this.loginForm.valid) {
      
      this.http.post('http://localhost:8080/api/v1/auth/login', this.loginForm.value).subscribe({
        next: (response: any) => {
          localStorage.setItem('token', response.data.token); // Store the JWT token
          localStorage.setItem('username', this.loginForm.value.username); // Store the username
          localStorage.setItem('role', this.loginForm.value.role); // Store the role
          // alert('Login successful');
          if (this.loginForm.value.role === 'Admin') {
            this.router.navigate(['/admin-dashboard']); // Navigate to the admin dashboard
          }
          else if (this.loginForm.value.role === 'Doctor') {
            this.router.navigate(['/doctor-dashboard']); // Navigate to the doctor dashboard
          } else if (this.loginForm.value.role === 'Patient') {
            this.router.navigate(['/patient-dashboard']); // Navigate to the patient dashboard
          }
        },
        error: (err) => {
          this.errorMessage = err.error.error || 'Login failed. Please try again.';
        }
      });
    }
  }

  
  
}


