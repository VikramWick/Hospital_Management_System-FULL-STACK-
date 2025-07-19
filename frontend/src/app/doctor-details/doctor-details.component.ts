import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-doctor-details',
  templateUrl: './doctor-details.component.html',
  styleUrls: ['./doctor-details.component.css']
})
export class DoctorDetailsComponent implements OnInit {
  doctors: any[] = []; // Array to store doctor details

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.fetchDoctors();
  }

  fetchDoctors(): void {
    this.http.get<any[]>('http://localhost:8080/api/v1/doctors-info').subscribe({
      next: (response) => {
        this.doctors = response;
      },
      error: (err) => {
        console.error('Error fetching doctor details:', err);
      }
    });
  }
}