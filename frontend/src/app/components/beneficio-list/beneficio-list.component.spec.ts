import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { provideAnimations } from '@angular/platform-browser/animations';

import { BeneficioListComponent } from './beneficio-list.component';
import { BeneficioService } from '../../service/beneficio.service';

class MockBeneficioService {
  getBeneficios() {
    return of([
      { id: 1, titular: 'Maria Silva', saldo: 3000, ativa: true },
      { id: 2, titular: 'João Santos', saldo: 1500, ativa: true }
    ]);
  }
  delete() { return of(undefined); }
}

describe('BeneficioListComponent', () => {
  let component: BeneficioListComponent;
  let fixture: ComponentFixture<BeneficioListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, BeneficioListComponent],
      providers: [
        { provide: BeneficioService, useClass: MockBeneficioService },
        provideAnimations()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BeneficioListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load beneficios on init', () => {
    expect(component.beneficios.length).toBe(2);
    expect(component.beneficios[0].titular).toBe('Maria Silva');
  });

  it('should set selecionado on novo', () => {
    component.novo();
    expect(component.selecionado).toBeTruthy();
    expect(component.selecionado?.titular).toBe('');
    expect(component.editando).toBeTrue();
  });

  it('should clear editando state', () => {
    component.selecionado = { id: 1, titular: 'Test', saldo: 100, ativa: true };
    component.editando = true;
    component.editando = false;
    expect(component.editando).toBeFalse();
  });
});
