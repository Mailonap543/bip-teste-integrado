import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { BeneficioListComponent } from './beneficio-list.component';
import { BeneficioService } from '../../service/beneficio.service';

interface Beneficio {
  id: number;
  nome: string;
  descricao: string;
  saldo: number;
  ativa: boolean;
  valor: number;
}

class MockBeneficioService {
  getBeneficios() {
    const mockData: Beneficio[] = [
      { id: 1, nome: 'Benefício 1', descricao: 'Teste', saldo: 100, ativa: true, valor: 50 },
      { id: 2, nome: 'Benefício 2', descricao: 'Teste 2', saldo: 200, ativa: true, valor: 150 }
    ];
    return of(mockData);
  }
}

describe('BeneficioListComponent', () => {
  let component: BeneficioListComponent;
  let fixture: ComponentFixture<BeneficioListComponent>;
  let service: BeneficioService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BeneficioListComponent], // ✅ standalone component aqui
      providers: [
        { provide: BeneficioService, useClass: MockBeneficioService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BeneficioListComponent);
    component = fixture.componentInstance;
    service = TestBed.inject(BeneficioService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load beneficios on init', () => {
    expect(component.beneficios.length).toBe(2);
    expect(component.beneficios[0].nome).toBe('Benefício 1');
  });
});
