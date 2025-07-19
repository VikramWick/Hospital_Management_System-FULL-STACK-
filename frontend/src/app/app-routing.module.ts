import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginPageComponent } from './login-page/login-page.component';
import { DoctorDashboardComponent } from './doctor-dashboard/doctor-dashboard.component';
import { PatientDashboardComponent } from './patient-dashboard/patient-dashboard.component';
import { HmsMainComponent } from './hms-main/hms-main.component'; // Import HmsMainComponent
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { DoctorDetailsComponent } from './doctor-details/doctor-details.component';
import { PharmacyPageComponent } from './pharmacy-page/pharmacy-page.component';
import { PharmacyPurchaseComponent } from './pharmacy-purchase/pharmacy-purchase.component';
import { InsurancePurchaseComponent } from './insurance-purchase/insurance-purchase.component';


const routes: Routes = [
  { path: '', redirectTo: '/hms-main', pathMatch: 'full' }, // Set HmsMainComponent as the default route
  { path: 'hms-main', component: HmsMainComponent }, // Route for HmsMainComponent
  { path: 'login', component: LoginPageComponent },
  { path: 'doctor-dashboard', component: DoctorDashboardComponent },
  { path: 'patient-dashboard', component: PatientDashboardComponent },
  {path:'admin-dashboard', component: AdminDashboardComponent},
  {path:'doctor-details',component:DoctorDetailsComponent}, // Route for AdminDashboardComponent
  {path:'pharmacy-page',component:PharmacyPageComponent}, // Route for PharmacyPageComponent
  { path: 'pharmacy-purchase', component: PharmacyPurchaseComponent },
  { path: 'insurance-purchase', component: InsurancePurchaseComponent },
  { path: '**', redirectTo: '/hms-main', pathMatch: 'full' } // Wildcard route to redirect to HmsMainComponent
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}