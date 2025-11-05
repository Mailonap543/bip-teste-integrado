import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { HomeComponent } from './home.component';
import { BeneficioListComponent } from '../beneficio-list/beneficio-list.component';
import { BeneficioService } from '../../service/beneficio.service';
import { of } from 'rxjs';

// Mock do serviço de Benefício
class MockBeneficioService {
  getBeneficios() {
    return of([]);
  }
}

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule, // ✅ Fornece ActivatedRoute e routerLink
        HttpClientTestingModule,
        HomeComponent,
        BeneficioListComponent
      ],
      providers: [
        { provide: BeneficioService, useClass: MockBeneficioService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
