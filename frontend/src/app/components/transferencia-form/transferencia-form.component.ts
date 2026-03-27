import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { BeneficioService } from '../../service/beneficio.service';
import { TransferenciaService } from '../../service/transferencia.service';
import { NotificationService } from '../../shared/services/notification.service';
import { Beneficio } from '../../models/beneficio.model';

@Component({
  selector: 'app-transferencia-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, CurrencyPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="page">
      <h1>Transferir Valor</h1>
      <p class="subtitle">Transfira valores entre beneficios de forma segura</p>
      <div class="transfer-card">
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label>Beneficio de Origem</label>
            <select formControlName="fromId">
              <option [ngValue]="null" disabled>Selecione a origem</option>
              <option *ngFor="let b of beneficios; trackBy: trackByBeneficio" [ngValue]="b.id">{{ b.titular }} - {{ b.saldo | currency:'BRL':'symbol':'1.2-2' }}</option>
            </select>
          </div>
          <div class="arrow">&#8597;</div>
          <div class="form-group">
            <label>Beneficio de Destino</label>
            <select formControlName="toId">
              <option [ngValue]="null" disabled>Selecione o destino</option>
              <option *ngFor="let b of beneficios; trackBy: trackByBeneficio" [ngValue]="b.id">{{ b.titular }} - {{ b.saldo | currency:'BRL':'symbol':'1.2-2' }}</option>
            </select>
          </div>
          <div class="form-group">
            <label>Valor da Transferencia</label>
            <input type="number" formControlName="amount" placeholder="0.00" step="0.01" min="0.01" />
          </div>
          <button type="submit" class="btn-transfer" [disabled]="form.invalid || loading">
            <span *ngIf="loading" class="spinner"></span>
            {{ loading ? 'Transferindo...' : 'Transferir' }}
          </button>
        </form>
      </div>
    </div>
  `,
  styles: [`
    h1 { margin: 0; font-size: 1.75rem; font-weight: 700; }
    .subtitle { color: var(--text-secondary); margin: 4px 0 24px; }
    .transfer-card { max-width: 500px; background: var(--bg-surface); padding: 24px; border-radius: 10px; box-shadow: var(--shadow-sm); }
    .form-group { margin-bottom: 16px; }
    .form-group label { display: block; font-weight: 600; margin-bottom: 6px; }
    .form-group select, .form-group input {
      width: 100%; padding: 10px 12px; border: 1px solid var(--border-color);
      border-radius: 6px; font-size: 1rem; box-sizing: border-box; background: var(--bg-surface); color: var(--text-primary);
      transition: border-color 0.2s, box-shadow 0.2s;
    }
    .form-group select:focus, .form-group input:focus { outline: none; border-color: var(--primary-500); box-shadow: 0 0 0 3px rgba(59,130,246,0.1); }
    .arrow { text-align: center; font-size: 1.5rem; color: var(--primary-500); padding: 4px 0; }
    .btn-transfer {
      width: 100%; padding: 12px; border: none; border-radius: 6px;
      cursor: pointer; font-weight: 600; font-size: 1rem;
      background: var(--warning-500); color: white; margin-top: 8px;
      transition: all 0.2s; display: flex; align-items: center; justify-content: center; gap: 8px;
    }
    .btn-transfer:hover { background: var(--warning-700); transform: translateY(-1px); }
    .btn-transfer:disabled { background: var(--neutral-400); cursor: not-allowed; transform: none; }
    .spinner {
      width: 18px; height: 18px; border: 2px solid rgba(255,255,255,0.3);
      border-top-color: white; border-radius: 50%; animation: spin 0.7s linear infinite;
    }
    @keyframes spin { to { transform: rotate(360deg); } }
  `]
})
export class TransferenciaFormComponent implements OnInit {
  form: FormGroup;
  beneficios: Beneficio[] = [];
  loading = false;

  constructor(
    private fb: FormBuilder,
    private beneficioService: BeneficioService,
    private transferenciaService: TransferenciaService,
    private notification: NotificationService,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({ fromId: [null, Validators.required], toId: [null, Validators.required], amount: [null, [Validators.required, Validators.min(0.01)]] });
  }

  trackByBeneficio(index: number, item: Beneficio): number | undefined { return item.id; }

  ngOnInit(): void {
    this.beneficioService.getBeneficios().subscribe({
      next: (d) => { this.beneficios = d.filter(b => b.ativa); this.cdr.markForCheck(); },
      error: () => { this.notification.error('Erro ao carregar beneficios'); this.cdr.markForCheck(); }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    const { fromId, toId, amount } = this.form.value;
    if (fromId === toId) { this.notification.warning('Origem e destino nao podem ser iguais'); return; }
    this.loading = true;
    this.transferenciaService.transferir(fromId, toId, amount).subscribe({
      next: () => { this.loading = false; this.notification.success('Transferencia realizada!'); this.form.reset(); this.ngOnInit(); this.cdr.markForCheck(); },
      error: () => { this.loading = false; this.cdr.markForCheck(); }
    });
  }
}
