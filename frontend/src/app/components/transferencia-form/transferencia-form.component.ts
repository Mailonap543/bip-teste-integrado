import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { BeneficioService } from '../../service/beneficio.service';
import { TransferenciaService } from '../../service/transferencia.service';
import { Beneficio } from '../../models/beneficio.model';

@Component({
  selector: 'app-transferencia-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './transferencia-form.component.html',
  styleUrls: ['./transferencia-form.component.css']
})
export class TransferenciaFormComponent implements OnInit {
  form!: FormGroup;
  beneficios: Beneficio[] = [];
  carregando = false;
  mensagem = '';
  tipoMensagem = '';

  constructor(
    private fb: FormBuilder,
    private beneficioService: BeneficioService,
    private transferenciaService: TransferenciaService
  ) {
    this.criarFormulario();
  }

  ngOnInit(): void {
    this.carregarBeneficios();
  }

  carregarBeneficios(): void {
    this.beneficioService.getBeneficios().subscribe({
      next: (data) => this.beneficios = data.filter(b => b.ativa),
      error: (err) => {
        console.error('Erro ao carregar benefícios', err);
        this.mostrarMensagem('Erro ao carregar benefícios', 'erro');
      }
    });
  }

  criarFormulario(): void {
    this.form = this.fb.group({
      fromId: [null, Validators.required],
      toId: [null, Validators.required],
      amount: [null, [Validators.required, Validators.min(0.01)]]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const { fromId, toId, amount } = this.form.value;

    if (fromId === toId) {
      this.mostrarMensagem('Origem e destino não podem ser iguais', 'erro');
      return;
    }

    this.carregando = true;
    this.transferenciaService.transferir(fromId, toId, amount).subscribe({
      next: () => {
        this.mostrarMensagem('Transferência realizada com sucesso!', 'sucesso');
        this.form.reset();
        this.carregando = false;
      },
      error: (err) => {
        const msg = err.error?.message || 'Erro ao realizar transferência';
        this.mostrarMensagem(msg, 'erro');
        this.carregando = false;
      }
    });
  }

  private mostrarMensagem(msg: string, tipo: string): void {
    this.mensagem = msg;
    this.tipoMensagem = tipo;
    setTimeout(() => {
      this.mensagem = '';
      this.tipoMensagem = '';
    }, 5000);
  }
}
