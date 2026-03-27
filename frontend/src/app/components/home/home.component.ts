import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { BeneficioService } from '../../service/beneficio.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, CurrencyPipe],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="dashboard">
      <div class="welcome-card">
        <div class="welcome-text">
          <h1>Bem-vindo ao BIP Beneficios</h1>
          <p>Gerencie seus beneficios e realize transferencias com seguranca</p>
        </div>
        <div class="welcome-actions">
          <a routerLink="/beneficios" class="btn-action primary">
            <span class="btn-icon">$</span> Beneficios
          </a>
          <a routerLink="/transferencias" class="btn-action secondary">
            <span class="btn-icon">&rarr;</span> Transferir
          </a>
        </div>
      </div>

      <div class="stats-grid">
        <div class="glass-card" *ngFor="let stat of stats; trackBy: trackByStat; let i = index"
             [style.animation-delay]="i * 0.12 + 's'" class="card-animate">
          <div class="glass-shine"></div>
          <div class="card-header">
            <div class="icon-container" [style.background]="stat.bg">
              <span class="card-icon" [style.color]="stat.color">{{ stat.icon }}</span>
            </div>
            <span class="card-label">{{ stat.label }}</span>
          </div>
          <div class="card-body">
            <h2 *ngIf="loading" class="skeleton-text"></h2>
            <h2 *ngIf="!loading" class="card-value" [style.color]="stat.color">{{ stat.value }}</h2>
            <div class="card-change" *ngIf="stat.change && !loading" [class.positive]="stat.changePositive">
              {{ stat.changePositive ? '&#9650;' : '&#9660;' }} {{ stat.change }}
            </div>
          </div>
        </div>
      </div>

      <div class="chart-section">
        <h3>Distribuicao de Saldos</h3>
        <div class="chart-container">
          <div class="bar-chart">
            <div class="bar-item" *ngFor="let b of topBeneficios; let i = index; trackBy: trackByBeneficio"
                 [style.animation-delay]="i * 0.15 + 's'">
              <div class="bar-label">{{ b.titular }}</div>
              <div class="bar-track">
                <div class="bar-fill" [style.width.%]="getBarWidth(b.saldo)"
                     [class.active]="b.ativa" [class.inactive]="!b.ativa">
                  <span class="bar-value">{{ b.saldo | currency:'BRL':'symbol':'1.0-0' }}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="chart-legend">
            <span class="legend-item"><span class="legend-dot active"></span> Ativa</span>
            <span class="legend-item"><span class="legend-dot inactive"></span> Inativa</span>
          </div>
        </div>
      </div>

      <div class="cards-row">
        <div class="glass-card info-card">
          <div class="glass-shine"></div>
          <h3>Beneficios Recentes</h3>
          <div class="recent-list" *ngIf="!loading; else recentSkeleton">
            <div class="recent-item" *ngFor="let b of beneficios; trackBy: trackByBeneficio; let i = index"
                 [style.animation-delay]="i * 0.08 + 's'" class="card-animate">
              <span class="recent-icon">$</span>
              <div class="recent-info">
                <span class="recent-name">{{ b.titular }}</span>
                <span class="recent-saldo">{{ b.saldo | currency:'BRL':'symbol':'1.2-2' }}</span>
              </div>
              <span class="badge" [class.active]="b.ativa">{{ b.ativa ? 'Ativa' : 'Inativa' }}</span>
            </div>
            <div class="empty" *ngIf="beneficios.length === 0">Nenhum beneficio cadastrado</div>
          </div>
          <ng-template #recentSkeleton>
            <div class="recent-list">
              <div class="recent-item skeleton-item" *ngFor="let s of [1,2,3]">
                <div class="skeleton-pulse" style="width:24px;height:24px;border-radius:6px"></div>
                <div class="recent-info" style="flex:1">
                  <div class="skeleton-pulse" style="height:14px;width:80%;border-radius:4px;margin-bottom:4px"></div>
                  <div class="skeleton-pulse" style="height:12px;width:50%;border-radius:4px"></div>
                </div>
              </div>
            </div>
          </ng-template>
          <a routerLink="/beneficios" class="card-link">Ver todos &rarr;</a>
        </div>

        <div class="glass-card info-card">
          <div class="glass-shine"></div>
          <h3>Acoes Rapidas</h3>
          <div class="quick-actions">
            <a routerLink="/beneficios" class="quick-btn">
              <span class="quick-icon">+</span>
              <span>Novo Beneficio</span>
            </a>
            <a routerLink="/transferencias" class="quick-btn">
              <span class="quick-icon">&rarr;</span>
              <span>Nova Transferencia</span>
            </a>
            <a routerLink="/about" class="quick-btn">
              <span class="quick-icon">#</span>
              <span>Relatorios</span>
            </a>
          </div>

          <div class="progress-section">
            <h4>Status do Sistema</h4>
            <div class="progress-item">
              <span>Beneficios Ativos</span>
              <div class="progress-bar">
                <div class="progress-fill success" [style.width.%]="activePercent"></div>
              </div>
              <span class="progress-text">{{ activePercent }}%</span>
            </div>
            <div class="progress-item">
              <span>Saldo Disponivel</span>
              <div class="progress-bar">
                <div class="progress-fill primary" [style.width.%]="balancePercent"></div>
              </div>
              <span class="progress-text">{{ balancePercent }}%</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dashboard { max-width: 1100px; margin: 0 auto; }
    .welcome-card {
      background: linear-gradient(135deg, #1e40af, #3b82f6, #6366f1);
      color: white; padding: 32px; border-radius: 20px;
      margin-bottom: 24px; display: flex; justify-content: space-between;
      align-items: center; flex-wrap: wrap; gap: 16px;
      box-shadow: 0 20px 50px rgba(30, 64, 175, 0.3);
      position: relative; overflow: hidden;
    }
    .welcome-card::before {
      content: ''; position: absolute; top: -50%; right: -20%;
      width: 300px; height: 300px;
      background: radial-gradient(circle, rgba(255,255,255,0.15) 0%, transparent 70%);
      border-radius: 50%;
    }
    .welcome-text h1 { font-size: 1.6rem; font-weight: 800; margin-bottom: 4px; }
    .welcome-text p { opacity: 0.85; margin: 0; }
    .welcome-actions { display: flex; gap: 10px; }
    .btn-action {
      padding: 10px 20px; border-radius: 12px; text-decoration: none;
      font-weight: 600; font-size: 0.9rem; transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      display: flex; align-items: center; gap: 8px;
    }
    .btn-action:hover { transform: translateY(-3px); text-decoration: none; box-shadow: 0 8px 20px rgba(0,0,0,0.2); }
    .btn-action.primary { background: rgba(255,255,255,0.2); color: white; backdrop-filter: blur(10px); }
    .btn-action.secondary { background: white; color: #1e40af; }
    .btn-icon { font-size: 1.1rem; }

    .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; margin-bottom: 24px; }

    .glass-card {
      position: relative; overflow: hidden;
      background: rgba(255, 255, 255, 0.03);
      backdrop-filter: blur(12px);
      -webkit-backdrop-filter: blur(12px);
      border: 1px solid rgba(255, 255, 255, 0.08);
      border-radius: 20px;
      padding: 24px;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      cursor: default;
    }
    .glass-card:hover {
      transform: translateY(-8px);
      background: rgba(255, 255, 255, 0.07);
      border-color: rgba(255, 255, 255, 0.15);
      box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4);
    }
    .glass-shine {
      position: absolute; top: 0; left: -100%; width: 50%; height: 100%;
      background: linear-gradient(to right, transparent, rgba(255, 255, 255, 0.04), transparent);
      transform: skewX(-25deg); transition: 0.7s;
    }
    .glass-card:hover .glass-shine { left: 150%; transition: 0.7s; }

    .card-animate { animation: cardSlideUp 0.4s ease-out both; }
    @keyframes cardSlideUp { from { opacity: 0; transform: translateY(20px); } to { opacity: 1; transform: translateY(0); } }

    .card-header { display: flex; align-items: center; gap: 12px; margin-bottom: 16px; }
    .icon-container {
      padding: 10px; border-radius: 12px;
      display: flex; align-items: center; justify-content: center;
      min-width: 44px; min-height: 44px;
    }
    .card-icon { font-size: 1.4rem; font-weight: 700; }
    .card-label {
      color: #94a3b8; font-size: 0.8rem; font-weight: 500;
      text-transform: uppercase; letter-spacing: 0.5px;
    }
    .card-body { min-height: 50px; }
    .card-value {
      font-size: 1.8rem; font-weight: 800; margin: 0;
      letter-spacing: -1px; line-height: 1.2;
    }
    .card-change { font-size: 0.75rem; margin-top: 6px; }
    .card-change.positive { color: #4ade80; }

    .skeleton-text {
      height: 32px; width: 80%;
      background: linear-gradient(90deg, rgba(255,255,255,0.05) 25%, rgba(255,255,255,0.1) 50%, rgba(255,255,255,0.05) 75%);
      background-size: 200% 100%; animation: skeletonLoad 1.5s infinite; border-radius: 8px;
    }
    @keyframes skeletonLoad { 0% { background-position: 200% 0; } 100% { background-position: -200% 0; } }

    .chart-section {
      background: rgba(255, 255, 255, 0.03); backdrop-filter: blur(12px);
      border: 1px solid rgba(255, 255, 255, 0.08);
      padding: 24px; border-radius: 20px; margin-bottom: 24px;
    }
    .chart-section h3 { margin: 0 0 16px; font-size: 1.1rem; }
    .bar-chart { display: flex; flex-direction: column; gap: 12px; }
    .bar-item { display: flex; align-items: center; gap: 12px; animation: cardSlideUp 0.3s ease-out both; }
    .bar-label { width: 120px; font-size: 0.85rem; font-weight: 500; text-align: right; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    .bar-track { flex: 1; height: 32px; background: rgba(255,255,255,0.05); border-radius: 8px; overflow: hidden; }
    .bar-fill {
      height: 100%; border-radius: 8px; display: flex; align-items: center; justify-content: flex-end;
      padding-right: 12px; transition: width 1s cubic-bezier(0.4, 0, 0.2, 1); min-width: 80px;
    }
    .bar-fill.active { background: linear-gradient(90deg, #10b981, #34d399); }
    .bar-fill.inactive { background: linear-gradient(90deg, #ef4444, #f87171); }
    .bar-value { font-size: 0.8rem; font-weight: 700; color: white; white-space: nowrap; }
    .chart-legend { display: flex; gap: 16px; margin-top: 12px; justify-content: center; }
    .legend-item { display: flex; align-items: center; gap: 6px; font-size: 0.8rem; color: var(--text-secondary); }
    .legend-dot { width: 10px; height: 10px; border-radius: 50%; }
    .legend-dot.active { background: #10b981; }
    .legend-dot.inactive { background: #ef4444; }

    .cards-row { display: grid; grid-template-columns: 2fr 1fr; gap: 20px; }
    .info-card h3 { margin: 0 0 16px; font-size: 1.1rem; }
    .recent-list { display: flex; flex-direction: column; gap: 8px; }
    .recent-item {
      display: flex; align-items: center; gap: 10px; padding: 12px;
      border-radius: 12px; background: rgba(255,255,255,0.05);
      transition: all 0.2s;
    }
    .recent-item:hover { transform: translateX(4px); background: rgba(255,255,255,0.08); }
    .recent-icon { font-size: 1.2rem; font-weight: 700; color: #3b82f6; min-width: 24px; text-align: center; }
    .recent-info { flex: 1; display: flex; flex-direction: column; }
    .recent-name { font-weight: 600; font-size: 0.9rem; }
    .recent-saldo { font-size: 0.8rem; color: var(--text-secondary); }
    .badge { padding: 3px 10px; border-radius: 20px; font-size: 0.7rem; font-weight: 600; }
    .badge.active { background: rgba(34, 197, 94, 0.15); color: #4ade80; }
    .badge:not(.active) { background: rgba(239, 68, 68, 0.15); color: #f87171; }
    .empty { text-align: center; color: var(--text-muted); padding: 20px; }
    .card-link { display: block; text-align: center; margin-top: 14px; color: #60a5fa; font-weight: 600; font-size: 0.9rem; }
    .card-link:hover { color: #93c5fd; }
    .quick-actions { display: flex; flex-direction: column; gap: 8px; margin-bottom: 20px; }
    .quick-btn {
      display: flex; align-items: center; gap: 10px; padding: 14px 16px;
      background: rgba(255,255,255,0.05); border-radius: 12px;
      text-decoration: none; color: var(--text-primary); font-weight: 500;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    }
    .quick-btn:hover { background: rgba(59, 130, 246, 0.2); color: white; text-decoration: none; transform: translateX(6px); }
    .quick-icon { font-size: 1.2rem; font-weight: 700; color: #3b82f6; min-width: 20px; text-align: center; }
    .progress-section { border-top: 1px solid rgba(255,255,255,0.08); padding-top: 16px; }
    .progress-section h4 { margin: 0 0 12px; font-size: 0.95rem; }
    .progress-item { display: flex; align-items: center; gap: 10px; margin-bottom: 10px; font-size: 0.85rem; }
    .progress-item > span:first-child { width: 120px; }
    .progress-bar { flex: 1; height: 6px; background: rgba(255,255,255,0.08); border-radius: 3px; overflow: hidden; }
    .progress-fill { height: 100%; border-radius: 3px; transition: width 1.2s cubic-bezier(0.4, 0, 0.2, 1); }
    .progress-fill.success { background: linear-gradient(90deg, #10b981, #34d399); }
    .progress-fill.primary { background: linear-gradient(90deg, #3b82f6, #60a5fa); }
    .progress-text { font-weight: 700; width: 40px; text-align: right; }
    .skeleton-pulse { background: linear-gradient(90deg, rgba(255,255,255,0.05) 25%, rgba(255,255,255,0.1) 50%, rgba(255,255,255,0.05) 75%); background-size: 200% 100%; animation: skeletonLoad 1.5s ease-in-out infinite; }
    @media (max-width: 768px) {
      .welcome-card { flex-direction: column; text-align: center; }
      .cards-row { grid-template-columns: 1fr; }
      .bar-label { width: 80px; font-size: 0.75rem; }
      .stats-grid { grid-template-columns: 1fr; }
    }
  `]
})
export class HomeComponent implements OnInit {
  loading = false;
  beneficios: any[] = [];
  topBeneficios: any[] = [];
  maxSaldo = 0;
  activePercent = 0;
  balancePercent = 0;
  stats = [
    { icon: '$', label: 'Total Beneficios', value: '0', bg: 'rgba(59,130,246,0.15)', color: '#60a5fa', change: 'carregando...', changePositive: true },
    { icon: '$', label: 'Saldo Total', value: 'R$ 0', bg: 'rgba(34,197,94,0.15)', color: '#4ade80' },
    { icon: '\u2192', label: 'Transferencias', value: '0', bg: 'rgba(251,191,36,0.15)', color: '#fbbf24' },
    { icon: '#', label: 'Media Saldo', value: 'R$ 0', bg: 'rgba(168,85,247,0.15)', color: '#c084fc' },
  ];

  constructor(private service: BeneficioService, private cdr: ChangeDetectorRef) {}

  trackByStat(index: number): number { return index; }
  trackByBeneficio(index: number, item: any): number | undefined { return item.id; }

  getBarWidth(saldo: number): number {
    return this.maxSaldo > 0 ? Math.max((saldo / this.maxSaldo) * 100, 15) : 15;
  }

  ngOnInit(): void {
    this.loading = true;
    this.service.getBeneficios().subscribe({
      next: (data) => {
        this.beneficios = data;
        this.topBeneficios = [...data].sort((a, b) => b.saldo - a.saldo).slice(0, 5);
        this.maxSaldo = Math.max(...data.map(b => b.saldo || 0), 1);
        const total = data.reduce((sum, b) => sum + (b.saldo || 0), 0);
        const ativos = data.filter(b => b.ativa).length;
        this.activePercent = data.length > 0 ? Math.round((ativos / data.length) * 100) : 0;
        this.balancePercent = total > 0 ? Math.min(Math.round((total / (total * 1.5)) * 100), 100) : 0;
        this.stats[0].value = String(data.length);
        this.stats[0].change = ativos + ' ativos';
        this.stats[1].value = 'R$ ' + total.toLocaleString('pt-BR', { minimumFractionDigits: 2 });
        this.stats[3].value = data.length > 0 ? 'R$ ' + (total / data.length).toLocaleString('pt-BR', { minimumFractionDigits: 2 }) : 'R$ 0';
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: () => { this.loading = false; this.cdr.markForCheck(); }
    });
  }
}
