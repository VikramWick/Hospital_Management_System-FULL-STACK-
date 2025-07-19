import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-hms-main',
  templateUrl: './hms-main.component.html',
  styleUrls: ['./hms-main.component.css']
})
export class HmsMainComponent {

  constructor(private router: Router) {}

  goToLogin() {
    this.router.navigate(['/login']); // adjust this route as per your routing
  }
}
