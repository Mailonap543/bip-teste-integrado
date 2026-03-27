import { Component, OnInit, OnDestroy, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, distinctUntilChanged, takeUntil } from 'rxjs';
import { BeneficioService } from '../../service/beneficio.service';
import { Beneficio } from '../../models/beneficio.model';
import { NotificationService } from '../../shared/services/notification.service';
import { BeneficioFormComponent } from '../beneficio-form/beneficio-form.component';

@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [CommonModule, CurrencyPipe, BeneficioFormComponent, FormsModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="page">
      <div class="page-header">
        <div><h1>Beneficios</h1><p class="subtitle">Gerencie seus beneficios</p></div>
        <div class="header-actions">
          <button class="btn-export" (click)="exportarCSV()">&#128190; Exportar CSV</button>
          <button class="btn-add" (click)="novo()" *ngIf="!editando">+ Novo Beneficio</button>
        </div>
      </div>

      <app-beneficio-form *ngIf="editando" [beneficio]="selecionado" (salvarBeneficio)="salvar($event)" (cancelar)="editando = false"></app-beneficio-form>

      <div *ngIf="!editando">
        <div class="filters-bar">
          <div class="search-box">
            <span class="search-icon">&#128269;</span>
            <input type="text" placeholder="Buscar por titular..." [(ngModel)]="filtroTitular" (ngModelChange)="onSearchChange($event)" />
          </div>
          <select [(ngModel)]="filtroStatus" (ngModelChange)="onFilterChange()">
            <option value="">Todos</option>
            <option value="true">Ativos</option>
            <option value="false">Inativos</option>
          </select>
          <select [(ngModel)]="sortBy" (ngModelChange)="onFilterChange()">
            <option value="id">Ordenar por ID</option>
            <option value="titular">Ordenar por Titular</option>
            <option value="saldo">Ordenar por Saldo</option>
          </select>
          <button class="btn-sort-dir" (click)="toggleDirection()">
            {{ sortDirection === 'asc' ? '&#8593;' : '&#8595;' }}
          </button>
        </div>

        <div *ngIf="loading" class="skeleton-grid">
          <div class="skeleton-card" *ngFor="let s of [1,2,3,4]">
            <div class="skeleton-header">
              <div class="skeleton-icon skeleton-pulse"></div>
              <div class="skeleton-title skeleton-pulse"></div>
              <div class="skeleton-badge skeleton-pulse"></div>
            </div>
            <div class="skeleton-body">
              <div class="skeleton-label skeleton-pulse"></div>
              <div class="skeleton-value skeleton-pulse"></div>
            </div>
            <div class="skeleton-actions">
              <div class="skeleton-btn skeleton-pulse"></div>
              <div class="skeleton-btn skeleton-pulse"></div>
            </div>
          </div>
        </div>

        <div *ngIf="!loading && beneficios.length === 0" class="empty">
          <div class="empty-icon">&#128203;</div>
          <h3>Nenhum beneficio encontrado</h3>
          <button class="btn-add" (click)="novo()">Criar Beneficio</button>
        </div>

        <div class="cards-grid" *ngIf="!loading && beneficios.length > 0">
          <div class="card" *ngFor="let b of beneficios; trackBy: trackByBeneficio">
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
              <button class="btn-delete" (click)="excluir(b.id!)">Excluir</button>
            </div>
          </div>
        </div>

        <div class="pagination" *ngIf="!loading && totalPages > 1">
          <button class="page-btn" [disabled]="currentPage === 0" (click)="goToPage(0)">&#171;</button>
          <button class="page-btn" [disabled]="currentPage === 0" (click)="goToPage(currentPage - 1)">&#8249;</button>
          <span class="page-info">Pagina {{ currentPage + 1 }} de {{ totalPages }} ({{ totalElements }} itens)</span>
          <button class="page-btn" [disabled]="currentPage >= totalPages - 1" (click)="goToPage(currentPage + 1)">&#8250;</button>
          <button class="page-btn" [disabled]="currentPage >= totalPages - 1" (click)="goToPage(totalPages - 1)">&#187;</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-wrap: wrap; gap: 12px; }
    .page-header h1 { margin: 0; font-size: 1.75rem; font-weight: 700; }
    .subtitle { margin: 4px 0 0; color: var(--text-secondary); }
    .header-actions { display: flex; gap: 10px; }
    .btn-add { background: var(--primary-500); color: white; border: none; padding: 10px 20px; border-radius: 6px; cursor: pointer; font-weight: 600; transition: all 0.2s; }
    .btn-add:hover { background: var(--primary-600); transform: translateY(-1px); }
    .btn-export { background: var(--success-500); color: white; border: none; padding: 10px 16px; border-radius: 6px; cursor: pointer; font-weight: 600; transition: all 0.2s; }
    .btn-export:hover { background: var(--success-700); transform: translateY(-1px); }
    .filters-bar { display: flex; gap: 12px; margin-bottom: 20px; flex-wrap: wrap; align-items: center; }
    .search-box { display: flex; align-items: center; gap: 8px; background: var(--bg-surface); border: 1px solid var(--border-color); border-radius: 6px; padding: 0 12px; flex: 1; min-width: 200px; }
    .search-box input { border: none; background: none; padding: 10px 0; flex: 1; font-size: 0.9rem; color: var(--text-primary); outline: none; }
    .search-icon { opacity: 0.5; }
    .filters-bar select { padding: 10px 12px; border: 1px solid var(--border-color); border-radius: 6px; background: var(--bg-surface); color: var(--text-primary); font-size: 0.9rem; }
    .btn-sort-dir { padding: 10px 14px; border: 1px solid var(--border-color); border-radius: 6px; background: var(--bg-surface); cursor: pointer; font-size: 1rem; transition: all 0.2s; }
    .btn-sort-dir:hover { background: var(--primary-500); color: white; }
    .empty { text-align: center; padding: 60px 20px; }
    .empty-icon { font-size: 3rem; margin-bottom: 12px; }
    .empty h3 { color: var(--text-primary); margin-bottom: 16px; }
    .cards-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }
    .card { background: var(--bg-surface); border-radius: 10px; box-shadow: var(--shadow-sm); overflow: hidden; transition: transform 0.2s ease, box-shadow 0.2s ease; }
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
    .btn-edit, .btn-delete { padding: 6px 14px; border: none; border-radius: 6px; cursor: pointer; font-weight: 500; font-size: 0.85rem; transition: all 0.2s; }
    .btn-edit { background: var(--warning-500); color: white; }
    .btn-delete { background: var(--error-500); color: white; }
    .btn-edit:hover { background: var(--warning-700); transform: scale(1.05); }
    .btn-delete:hover { background: var(--error-700); transform: scale(1.05); }
    .pagination { display: flex; align-items: center; justify-content: center; gap: 8px; margin-top: 24px; padding: 16px 0; }
    .page-btn { padding: 8px 14px; border: 1px solid var(--border-color); border-radius: 6px; background: var(--bg-surface); cursor: pointer; transition: all 0.2s; }
    .page-btn:hover:not(:disabled) { background: var(--primary-500); color: white; border-color: var(--primary-500); }
    .page-btn:disabled { opacity: 0.4; cursor: not-allowed; }
    .page-info { font-size: 0.85rem; color: var(--text-secondary); margin: 0 12px; }
    .skeleton-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }
    .skeleton-card { background: var(--bg-surface); border-radius: 10px; box-shadow: var(--shadow-sm); overflow: hidden; }
    .skeleton-header { display: flex; align-items: center; gap: 10px; padding: 16px; border-bottom: 1px solid var(--divider); }
    .skeleton-icon { width: 28px; height: 28px; border-radius: 6px; }
    .skeleton-title { height: 16px; flex: 1; border-radius: 4px; }
    .skeleton-badge { width: 60px; height: 22px; border-radius: 12px; }
    .skeleton-body { padding: 16px; }
    .skeleton-label { height: 12px; width: 100px; border-radius: 4px; margin-bottom: 8px; }
    .skeleton-value { height: 24px; width: 140px; border-radius: 4px; }
    .skeleton-actions { display: flex; gap: 8px; padding: 12px 16px; border-top: 1px solid var(--divider); justify-content: flex-end; }
    .skeleton-btn { width: 70px; height: 30px; border-radius: 6px; }
    .skeleton-pulse { background: linear-gradient(90deg, var(--bg-surface-secondary) 25%, var(--divider) 50%, var(--bg-surface-secondary) 75%); background-size: 200% 100%; animation: pulse 1.5s ease-in-out infinite; }
    @keyframes pulse { 0% { background-position: 200% 0; } 100% { background-position: -200% 0; } }
  `]
})
export class BeneficioListComponent implements OnInit, OnDestroy {
  beneficios: Beneficio[] = [];
  selecionado: Beneficio | null = null;
  editando = false;
  loading = false;

  currentPage = 0;
  pageSize = 12;
  totalPages = 0;
  totalElements = 0;
  sortBy = 'id';
  sortDirection = 'asc';
  filtroTitular = '';
  filtroStatus = '';

  private searchSubject = new Subject<string>();
  private destroy$ = new Subject<void>();

  constructor(private service: BeneficioService, private notification: NotificationService, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.searchSubject.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.currentPage = 0;
      this.carregar();
    });
    this.carregar();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  trackByBeneficio(index: number, item: Beneficio): number | undefined { return item.id; }

  carregar(): void {
    this.loading = true;
    this.service.getBeneficiosPaged(this.currentPage, this.pageSize, this.sortBy, this.sortDirection).subscribe({
      next: (d) => {
        this.beneficios = this.filtrarLocal(d.content);
        this.totalPages = d.totalPages;
        this.totalElements = d.totalElements;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => { this.loading = false; this.cdr.markForCheck(); }
    });
  }

  filtrarLocal(items: Beneficio[]): Beneficio[] {
    let result = items;
    if (this.filtroTitular) {
      result = result.filter(b => b.titular.toLowerCase().includes(this.filtroTitular.toLowerCase()));
    }
    if (this.filtroStatus) {
      const ativa = this.filtroStatus === 'true';
      result = result.filter(b => b.ativa === ativa);
    }
    return result;
  }

  onSearchChange(value: string): void {
    this.searchSubject.next(value);
  }

  onFilterChange(): void {
    this.currentPage = 0;
    this.carregar();
  }

  toggleDirection(): void {
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.carregar();
  }

  goToPage(page: number): void {
    this.currentPage = page;
    this.carregar();
  }

  novo(): void { this.selecionado = { id: undefined, titular: '', saldo: 0, ativa: true }; this.editando = true; this.cdr.markForCheck(); }

  editar(b: Beneficio): void { this.selecionado = { ...b }; this.editando = true; this.cdr.markForCheck(); }

  salvar(b: Beneficio): void {
    const op = b.id ? this.service.update(b) : this.service.create(b);
    op.subscribe({
      next: () => { this.notification.success(b.id ? 'Atualizado!' : 'Criado!'); this.editando = false; this.carregar(); },
      error: () => { this.notification.error('Erro ao salvar'); this.cdr.markForCheck(); }
    });
  }

  excluir(id: number): void {
    if (!confirm('Deseja excluir?')) return;
    const backup = [...this.beneficios];
    this.service.delete(id).subscribe({
      next: () => {
        this.notification.success('Excluido!', () => {
          this.notification.info('Operação de desfazer não disponível para exclusão');
        });
        this.carregar();
      },
      error: () => { this.notification.error('Erro ao excluir'); this.cdr.markForCheck(); }
    });
  }

  exportarCSV(): void {
    const headers = ['ID', 'Titular', 'Saldo', 'Ativa'];
    const rows = this.beneficios.map(b => [b.id, b.titular, b.saldo, b.ativa ? 'Sim' : 'Não']);
    const csv = [headers.join(','), ...rows.map(r => r.join(','))].join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = `beneficios_${new Date().toISOString().split('T')[0]}.csv`;
    link.click();
    URL.revokeObjectURL(url);
    this.notification.success('CSV exportado com sucesso!');
  }
}
