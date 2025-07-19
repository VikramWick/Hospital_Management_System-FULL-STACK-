import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InsurancePurchaseComponent } from './insurance-purchase.component';

describe('InsurancePurchaseComponent', () => {
  let component: InsurancePurchaseComponent;
  let fixture: ComponentFixture<InsurancePurchaseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InsurancePurchaseComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(InsurancePurchaseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
