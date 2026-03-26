import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { BeneficioFormComponent } from './beneficio-form.component';
import { Beneficio } from '../../models/beneficio.model';

describe('BeneficioFormComponent', () => {
  let component: BeneficioFormComponent;
  let fixture: ComponentFixture<BeneficioFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CommonModule,
        ReactiveFormsModule,
        BeneficioFormComponent
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BeneficioFormComponent);
    component = fixture.componentInstance;

    const beneficioTeste: Beneficio = {
      id: 1,
      titular: 'Maria Silva',
      saldo: 3000,
      ativa: true
    };

    component.beneficio = beneficioTeste;
    component.ngOnChanges({
      beneficio: {
        currentValue: beneficioTeste,
        previousValue: null,
        firstChange: true,
        isFirstChange: () => true
      }
    });

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit salvarBeneficio when form is valid', () => {
    spyOn(component.salvarBeneficio, 'emit');
    component.onSubmit();
    expect(component.salvarBeneficio.emit).toHaveBeenCalled();
  });

  it('should emit cancelar when onCancel is called', () => {
    spyOn(component.cancelar, 'emit');
    component.onCancel();
    expect(component.cancelar.emit).toHaveBeenCalled();
  });

  it('should validate required titular', () => {
    component.form.get('titular')?.setValue('');
    expect(component.form.get('titular')?.valid).toBeFalse();
  });

  it('should validate titular minlength', () => {
    component.form.get('titular')?.setValue('ab');
    expect(component.form.get('titular')?.valid).toBeFalse();
  });

  it('should validate saldo min', () => {
    component.form.get('saldo')?.setValue(-1);
    expect(component.form.get('saldo')?.valid).toBeFalse();
  });
});
