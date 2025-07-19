import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HmsMainComponent } from './hms-main.component';

describe('HmsMainComponent', () => {
  let component: HmsMainComponent;
  let fixture: ComponentFixture<HmsMainComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HmsMainComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HmsMainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
