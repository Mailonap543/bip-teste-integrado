import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Beneficio } from '../../models/beneficio.model';

@Component({
  selector: 'app-beneficio-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './beneficio-form.component.html',
  styleUrls: ['./beneficio-form.component.css']
})
export class BeneficioFormComponent implements OnChanges {
  @Input() beneficio: Beneficio | null = null;
  @Output() salvarBeneficio = new EventEmitter<Beneficio>();
  @Output() cancelar = new EventEmitter<void>();

  form!: FormGroup;

  constructor(private fb: FormBuilder) {
    this.criarFormulario();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['beneficio'] && this.form) {
      this.form.patchValue(this.beneficio ?? {
        id: undefined,
        titular: '',
        saldo: 0,
        ativa: true
      });
    }
  }

  criarFormulario(): void {
    this.form = this.fb.group({
      id: [undefined],
      titular: ['', [Validators.required, Validators.minLength(3)]],
      saldo: [0, [Validators.required, Validators.min(0)]],
      ativa: [true]
    });
  }

  onSubmit(): void {
    if (this.form.valid) {
      this.salvarBeneficio.emit(this.form.value as Beneficio);
    } else {
      this.form.markAllAsTouched();
    }
  }

  onCancel(): void {
    this.cancelar.emit();
  }

  get isEdicao(): boolean {
    return !!this.form?.value?.id;
  }
}
