import { Component, OnInit } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { BeneficioService } from '../../service/beneficio.service';
import { Beneficio } from '../../models/beneficio.model';
import { NotificationService } from '../../shared/services/notification.service';
import { BeneficioFormComponent } from '../beneficio-form/beneficio-form.component';

@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, BeneficioFormComponent],
  template: `
    <div class="page">
      <div class="page-header">
        <div><h1>Beneficios</h1><p class="subtitle">Gerencie seus beneficios</p></div>
        <button class="btn-add" (click)="novo()" *ngIf="!editando">+ Novo Beneficio</button>
      </div>
      <app-beneficio-form *ngIf="editando" [beneficio]="selecionado" (salvarBeneficio)="salvar($event)" (cancelar)="editando = false"></app-beneficio-form>
      <div *ngIf="!editando">
        <div *ngIf="loading" class="loading">Carregando...</div>
        <div *ngIf="!loading && beneficios.length === 0" class="empty">
          <div class="empty-icon">&#128203;</div>
          <h3>Nenhum beneficio encontrado</h3>
          <button class="btn-add" (click)="novo()">Criar Beneficio</button>
        </div>
        <div class="cards-grid" *ngIf="!loading && beneficios.length > 0">
          <div class="card" *ngFor="let b of beneficios">
            <div class="card-header">
              <span class="card-icon">&#128176;</span>
              <span class="card-title">{{ b.titular }}</span>
              <span class="badge" [class.ativa]="b.ativa" [class.inativa]="!b.ativa">{{ b.ativa ? 'Ativa' : 'Inativa' }}</span>
            </div>
            <div class="card-body">
              <span class="saldo-label">Saldo Disponivel</span>
              <span class="saldo-value">{{ b.saldo | currency:'BRL':'symbol':'1.2-2' }}</span>
            </div>
            <div class="card-actions">
              <button class="btn-edit" (click)="editar(b)">Editar</button>
              <button class="btn-delete" (click)="excluir(b.id)">Excluir</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-wrap: wrap; gap: 12px; }
    .page-header h1 { margin: 0; font-size: 1.75rem; font-weight: 700; }
    .subtitle { margin: 4px 0 0; color: var(--text-secondary); }
    .btn-add { background: var(--primary-500); color: white; border: none; padding: 10px 20px; border-radius: 6px; cursor: pointer; font-weight: 600; }
    .btn-add:hover { background: var(--primary-600); }
    .loading { text-align: center; padding: 60px; color: var(--text-secondary); }
    .empty { text-align: center; padding: 60px 20px; }
    .empty-icon { font-size: 3rem; margin-bottom: 12px; }
    .empty h3 { color: var(--text-primary); margin-bottom: 16px; }
    .cards-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }
    .card { background: var(--bg-surface); border-radius: 10px; box-shadow: var(--shadow-sm); overflow: hidden; transition: transform 0.2s; }
    .card:hover { transform: translateY(-4px); box-shadow: var(--shadow-md); }
    .card-header { display: flex; align-items: center; gap: 10px; padding: 16px; border-bottom: 1px solid var(--divider); }
    .card-icon { font-size: 1.3rem; }
    .card-title { font-weight: 600; flex: 1; color: var(--text-primary); }
    .badge { padding: 3px 10px; border-radius: 12px; font-size: 0.8rem; font-weight: 500; }
    .badge.ativa { background: var(--success-50); color: var(--success-700); }
    .badge.inativa { background: var(--error-50); color: var(--error-700); }
    .card-body { padding: 16px; }
    .saldo-label { display: block; font-size: 0.8rem; color: var(--text-muted); margin-bottom: 4px; }
    .saldo-value { font-size: 1.5rem; font-weight: 700; color: var(--success-700); }
    .card-actions { display: flex; gap: 8px; padding: 12px 16px; border-top: 1px solid var(--divider); justify-content: flex-end; }
    .btn-edit, .btn-delete { padding: 6px 14px; border: none; border-radius: 6px; cursor: pointer; font-weight: 500; font-size: 0.85rem; }
    .btn-edit { background: var(--warning-500); color: white; }
    .btn-delete { background: var(--error-500); color: white; }
    .btn-edit:hover { background: var(--warning-700); }
    .btn-delete:hover { background: var(--error-700); }
  `]
})
export class BeneficioListComponent implements OnInit {
  beneficios: Beneficio[] = [];
  selecionado: Beneficio | null = null;
  editando = false;
  loading = false;

  constructor(private service: BeneficioService, private notification: NotificationService) {}

  ngOnInit(): void { this.carregar(); }

  carregar(): void {
    this.loading = true;
    this.service.getBeneficios().subscribe({ next: (d) => { this.beneficios = d; this.loading = false; }, error: () => this.loading = false });
  }

  novo(): void { this.selecionado = { id: undefined, titular: '', saldo: 0, ativa: true }; this.editando = true; }

  editar(b: Beneficio): void { this.selecionado = { ...b }; this.editando = true; }

  salvar(b: Beneficio): void {
    const op = b.id ? this.service.update(b) : this.service.create(b);
    op.subscribe({ next: () => { this.notification.success(b.id ? 'Atualizado!' : 'Criado!'); this.editando = false; this.carregar(); }, error: () => this.notification.error('Erro ao salvar') });
  }

  excluir(id?: number): void {
    if (!id || !confirm('Deseja excluir?')) return;
    this.service.delete(id).subscribe({ next: () => { this.notification.success('Excluido!'); this.carregar(); }, error: () => this.notification.error('Erro ao excluir') });
  }
}
