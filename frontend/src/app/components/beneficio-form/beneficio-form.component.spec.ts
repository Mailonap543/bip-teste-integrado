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
        BeneficioFormComponent // ✅ standalone component
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(BeneficioFormComponent);
    component = fixture.componentInstance;

    // Criar objeto de teste para evitar problemas de tipagem
    const beneficioTeste: Beneficio = {
      id: 1,
      nome: 'Vale Alimentação',
      descricao: 'Benefício para alimentação',
      saldo: 200,
      valor: 200,
      ativa: true
    };

    // Atribuir e disparar ngOnChanges manualmente
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
    const beneficioEmitir: Beneficio = { ...component.beneficio! }; // garante tipagem correta
    spyOn(component.salvarBeneficio, 'emit');

    component.onSubmit();

    expect(component.salvarBeneficio.emit).toHaveBeenCalledWith(beneficioEmitir);
  });

  it('should emit cancelar when onCancel is called', () => {
    spyOn(component.cancelar, 'emit');

    component.onCancel();

    expect(component.cancelar.emit).toHaveBeenCalled();
  });
});
