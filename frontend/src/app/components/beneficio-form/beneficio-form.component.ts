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
        nome: '',
        descricao: '',
        saldo: 0,
        ativa: true,
        valor: 0
      });
    }
  }

  criarFormulario(): void {
    this.form = this.fb.group({
      id: [undefined],
      nome: ['', [Validators.required, Validators.minLength(3)]],
      descricao: ['', Validators.required],
      saldo: [0, Validators.min(0)],
      valor: [0, Validators.required],
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
}
