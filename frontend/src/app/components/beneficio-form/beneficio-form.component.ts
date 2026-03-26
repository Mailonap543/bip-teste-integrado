import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Beneficio } from '../../models/beneficio.model';

@Component({
  selector: 'app-beneficio-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="form-card">
      <h2>{{ isEdicao ? 'Editar Beneficio' : 'Novo Beneficio' }}</h2>
      <form [formGroup]="form" (ngSubmit)="onSubmit()">
        <div class="form-group">
          <label>Titular</label>
          <input type="text" formControlName="titular" placeholder="Nome do titular" />
          <div class="error" *ngIf="form.get('titular')?.touched && form.get('titular')?.invalid">
            <span *ngIf="form.get('titular')?.errors?.['required']">Obrigatorio</span>
            <span *ngIf="form.get('titular')?.errors?.['minlength']">Minimo 3 caracteres</span>
          </div>
        </div>
        <div class="form-group">
          <label>Saldo</label>
          <input type="number" formControlName="saldo" placeholder="0.00" step="0.01" />
          <div class="error" *ngIf="form.get('saldo')?.touched && form.get('saldo')?.invalid">Obrigatorio</div>
        </div>
        <div class="form-group">
          <label class="checkbox-label"><input type="checkbox" formControlName="ativa" /> Beneficio Ativo</label>
        </div>
        <div class="form-actions">
          <button type="button" class="btn-cancel" (click)="onCancel()">Cancelar</button>
          <button type="submit" class="btn-save" [disabled]="form.invalid">{{ isEdicao ? 'Atualizar' : 'Criar' }}</button>
        </div>
      </form>
    </div>
  `,
  styles: [`
    .form-card { max-width: 500px; margin: 0 auto 24px; background: var(--bg-surface); padding: 24px; border-radius: 10px; box-shadow: var(--shadow-sm); }
    .form-card h2 { margin: 0 0 20px; font-size: 1.3rem; }
    .form-group { margin-bottom: 16px; }
    .form-group label { display: block; font-weight: 600; margin-bottom: 6px; color: var(--text-primary); }
    .form-group input[type="text"], .form-group input[type="number"] {
      width: 100%; padding: 10px 12px; border: 1px solid var(--border-color);
      border-radius: 6px; font-size: 1rem; box-sizing: border-box; background: var(--bg-surface); color: var(--text-primary);
    }
    .form-group input:focus { outline: none; border-color: var(--primary-500); }
    .checkbox-label { display: flex; align-items: center; gap: 8px; cursor: pointer; }
    .checkbox-label input { width: 18px; height: 18px; }
    .error { color: var(--error-500); font-size: 0.85rem; margin-top: 4px; }
    .form-actions { display: flex; gap: 12px; justify-content: flex-end; margin-top: 20px; padding-top: 16px; border-top: 1px solid var(--divider); }
    .btn-cancel, .btn-save { padding: 10px 20px; border: none; border-radius: 6px; cursor: pointer; font-weight: 600; }
    .btn-cancel { background: var(--neutral-300); color: var(--text-primary); }
    .btn-save { background: var(--primary-500); color: white; }
    .btn-save:disabled { background: var(--neutral-400); }
  `]
})
export class BeneficioFormComponent implements OnChanges {
  @Input() beneficio: Beneficio | null = null;
  @Output() salvarBeneficio = new EventEmitter<Beneficio>();
  @Output() cancelar = new EventEmitter<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({ id: [undefined], titular: ['', [Validators.required, Validators.minLength(3)]], saldo: [0, [Validators.required, Validators.min(0)]], ativa: [true] });
  }

  ngOnChanges(changes: SimpleChanges): void { if (changes['beneficio'] && this.beneficio) this.form.patchValue(this.beneficio); }

  get isEdicao(): boolean { return !!this.form.value.id; }

  onSubmit(): void { if (this.form.valid) this.salvarBeneficio.emit(this.form.value); else this.form.markAllAsTouched(); }
  onCancel(): void { this.cancelar.emit(); }
}
