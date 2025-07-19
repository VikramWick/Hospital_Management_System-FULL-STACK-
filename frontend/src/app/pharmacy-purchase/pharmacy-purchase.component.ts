import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface Medicine {
  id: number;
  name: string;
  price: number;
  quantity: number;
  selectedQuantity?: number;
}

interface CartItem {
  medicine: Medicine;
  quantity: number;
}

@Component({
  selector: 'app-pharmacy-purchase',
  templateUrl: './pharmacy-purchase.component.html',
  styleUrls: ['./pharmacy-purchase.component.css']
})
export class PharmacyPurchaseComponent implements OnInit {
  medicines: Medicine[] = [];
  cart: CartItem[] = [];
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

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchMedicines();
  }

  fetchMedicines(): void {
    this.http.get<Medicine[]>('http://localhost:8082/api/pharmacy/medicines').subscribe({
      next: (data) => {
        this.medicines = data.map(med => ({...med, selectedQuantity: 0}));
      },
      error: (error) => {
        console.error('Error fetching medicines:', error);
        alert('Failed to fetch medicines');
      }
    });
  }

  addToCart(medicine: Medicine): void {
    if (!medicine.selectedQuantity || medicine.selectedQuantity <= 0) {
      alert('Please select a valid quantity');
      return;
    }

    const existingItem = this.cart.find(item => item.medicine.id === medicine.id);
    if (existingItem) {
      existingItem.quantity += medicine.selectedQuantity;
    } else {
      this.cart.push({
        medicine: {...medicine},
        quantity: medicine.selectedQuantity
      });
    }

    this.calculateTotal();
    medicine.selectedQuantity = 0;
    alert('Added to cart!');
  }

  calculateTotal(): void {
    this.totalAmount = this.cart.reduce((sum, item) => 
      sum + (item.medicine.price * item.quantity), 0);
  }

  removeFromCart(index: number): void {
    this.cart.splice(index, 1);
    this.calculateTotal();
  }

  processPayment(): void {
    if (!this.paymentMethod) {
      alert('Please select a payment method');
      return;
    }

    if (this.paymentMethod === 'upi' && !this.paymentDetails.upiId) {
      alert('Please enter UPI ID');
      return;
    }

    if (this.paymentMethod === 'card') {
      if (!this.paymentDetails.cardNumber || 
          !this.paymentDetails.cardName || 
          !this.paymentDetails.expiryDate || 
          !this.paymentDetails.cvv) {
        alert('Please fill all card details');
        return;
      }
    }

    // Here you would typically make an API call to process the payment
    alert('Payment processed successfully!');
    this.cart = [];
    this.totalAmount = 0;
    this.showPaymentModal = false;
    this.paymentMethod = null;
    this.router.navigate(['/patient-dashboard']);
  }

  goBack(): void {
    this.router.navigate(['/patient-dashboard']);
  }
}