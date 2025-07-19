import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
// import { ToastrModule } from 'ngx-toastr';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginPageComponent } from './login-page/login-page.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DoctorDashboardComponent } from './doctor-dashboard/doctor-dashboard.component';
import { PatientDashboardComponent } from './patient-dashboard/patient-dashboard.component';
import { HmsMainComponent } from './hms-main/hms-main.component';
import { AdminDashboardComponent } from './admin-dashboard/admin-dashboard.component';
import { DoctorDetailsComponent } from './doctor-details/doctor-details.component';
import { EmergencyComponent } from './emergency/emergency.component';
import { PharmacyPageComponent } from './pharmacy-page/pharmacy-page.component';
import { PharmacyPurchaseComponent } from './pharmacy-purchase/pharmacy-purchase.component';
import { InsurancePurchaseComponent } from './insurance-purchase/insurance-purchase.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginPageComponent,
    DoctorDashboardComponent,
    PatientDashboardComponent,
    HmsMainComponent,
    AdminDashboardComponent,
    DoctorDetailsComponent,
    EmergencyComponent,
    PharmacyPageComponent,
    PharmacyPurchaseComponent,
    InsurancePurchaseComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
