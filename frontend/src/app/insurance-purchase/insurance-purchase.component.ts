import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

interface InsurancePlan {
  id: number;
  name: string;
  premium: number;
  coverage: string[];
  duration: string;
  selected?: boolean; // Optional property to track selection
}

@Component({
  selector: 'app-insurance-purchase',
  templateUrl: './insurance-purchase.component.html',
  styleUrls: ['./insurance-purchase.component.css']
})
export class InsurancePurchaseComponent implements OnInit {

  

  insurancePlans: InsurancePlan[] = [
    {
      id: 1,
      name: 'Basic Health Insurance',
      premium: 500,
      coverage: ['General Medical Care', 'Emergency Services', 'Basic Medications'],
      duration: '1 Year'
    },
    {
      id: 2,
      name: 'Premium Health Insurance',
      premium: 1200,
      coverage: ['Comprehensive Coverage', 'Specialist Consultations', 'Advanced Treatments'],
      duration: '1 Year'
    },
    {
      id: 3,
      name: 'Family Health Insurance',
      premium: 2000,
      coverage: ['Coverage for Entire Family', 'Maternity Benefits', 'Child Care', 'Regular Health Checkups'],
      duration: '1 Year'
    },
    {
      id: 4,
      name: 'Senior Citizen Health Insurance',
      premium: 800,
      coverage: ['Age-specific Coverage', 'Chronic Disease Management', 'Regular Health Monitoring'],
      duration: '1 Year'
    },
    {
      id: 5,
      name: 'Term Life Insurance',
      premium: 1000,
      coverage: ['Death Benefit', 'Accident Coverage', 'Terminal Illness Benefit'],
      duration: '1 Year'
    },
    {
      id: 6,
      name: 'Whole Life Insurance',
      premium: 1500,
      coverage: ['Lifetime Coverage', 'Cash Value Benefits', 'Policy Dividends'],
      duration: 'Lifetime'
    },
    {
      id: 7,
      name: 'Critical Illness Insurance',
      premium: 750,
      coverage: ['Major Disease Coverage', 'Treatment Cost Coverage', 'Recovery Support'],
      duration: '1 Year'
    },
    {
      id: 8,
      name: 'Dental Insurance',
      premium: 300,
      coverage: ['Regular Checkups', 'Dental Procedures', 'Orthodontic Care'],
      duration: '1 Year'
    },
    {
      id: 9,
      name: 'Vision Insurance',
      premium: 250,
      coverage: ['Eye Examinations', 'Prescription Glasses', 'Contact Lenses'],
      duration: '1 Year'
    }
    // Add more plans as needed
  ];

  selectedPlan: InsurancePlan | null = null;
  selectedPlans: InsurancePlan[] = [];
  showPaymentModal = false;
  paymentMethod: 'upi' | 'card' | null = null;
  totalAmount = 0;
  paymentDetails = {
    upiId: '',
    cardNumber: '',
    cardName: '',
    expiryDate: '',
    cvv: ''
  };

  constructor(private router: Router) {}

  ngOnInit(): void {}

  selectPlan(plan: InsurancePlan): void {
    plan.selected = !plan.selected; // Toggle selection
    
    if (plan.selected) {
      this.selectedPlans.push(plan);
    } else {
      this.selectedPlans = this.selectedPlans.filter(p => p.id !== plan.id);
    }
    
    // Recalculate total amount
    this.totalAmount = this.selectedPlans.reduce((sum, p) => sum + p.premium, 0);
  }

  processPayment(): void {
    if (!this.paymentMethod) {
      this.showError('Please select a payment method');
      return;
    }

    if (this.paymentMethod === 'upi' && !this.paymentDetails.upiId) {
      this.showError('Please enter UPI ID');
      return;
    }

    if (this.paymentMethod === 'card') {
      if (!this.paymentDetails.cardNumber || 
          !this.paymentDetails.cardName || 
          !this.paymentDetails.expiryDate || 
          !this.paymentDetails.cvv) {
        this.showError('Please fill all card details');
        return;
      }
    }

    this.showSuccess();
  }

  showSuccess(): void {
    alert(`Payment of ₹${this.totalAmount} processed successfully! Your insurance is now active.`);
    this.router.navigate(['/patient-dashboard']);
  }

  showError(message: string): void {
    alert(message);
  }

  goBack(): void {
    this.router.navigate(['/patient-dashboard']);
  }
}