import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <aside class="sidebar" [class.collapsed]="collapsed" [class.mobile-open]="mobileOpen">
      <div class="backdrop" *ngIf="mobileOpen" (click)="mobileOpen = false"></div>
      <div class="sidebar-inner">
        <div class="sidebar-section">
          <div class="section-label" *ngIf="!collapsed">NAVEGAÇÃO</div>
          <nav class="sidebar-nav">
            <a *ngFor="let item of menu" [routerLink]="item.route" routerLinkActive="active"
               [routerLinkActiveOptions]="{exact: item.exact || false}" class="nav-item" (click)="mobileOpen = false">
              <span class="nav-icon">{{ item.icon }}</span>
              <span class="nav-label" *ngIf="!collapsed">{{ item.label }}</span>
              <span class="nav-badge" *ngIf="item.badge && !collapsed">{{ item.badge }}</span>
            </a>
          </nav>
        </div>

        <div class="sidebar-section stats" *ngIf="!collapsed">
          <div class="section-label">RESUMO</div>
          <div class="stat-card">
            <div class="stat-label">Total Benefícios</div>
            <div class="stat-value">3</div>
          </div>
          <div class="stat-card">
            <div class="stat-label">Saldo Total</div>
            <div class="stat-value success">R$ 5.000,00</div>
          </div>
          <div class="stat-card">
            <div class="stat-label">Transferências Hoje</div>
            <div class="stat-value">0</div>
          </div>
        </div>

        <div class="sidebar-footer" *ngIf="!collapsed">
          <div class="version">v1.0.0</div>
        </div>
      </div>
    </aside>
  `,
  styles: [`
    .sidebar {
      position: fixed; top: 64px; left: 0; bottom: 0; width: 240px;
      background: var(--bg-surface); border-right: 1px solid var(--border-color);
      transition: width 0.25s ease; z-index: 900; overflow: hidden;
    }
    .sidebar.collapsed { width: 60px; }
    .sidebar-inner { height: 100%; display: flex; flex-direction: column; padding: 16px 8px; overflow-y: auto; }
    .sidebar-section { margin-bottom: 20px; }
    .section-label {
      font-size: 0.65rem; font-weight: 700; letter-spacing: 1px;
      color: var(--text-muted); padding: 8px 12px; text-transform: uppercase;
    }
    .sidebar-nav { display: flex; flex-direction: column; gap: 2px; }
    .nav-item {
      display: flex; align-items: center; gap: 12px; padding: 10px 12px;
      border-radius: 8px; color: var(--text-secondary); text-decoration: none;
      transition: all 0.15s; position: relative;
    }
    .nav-item:hover { background: var(--bg-surface-secondary); color: var(--text-primary); text-decoration: none; }
    .nav-item.active {
      background: linear-gradient(135deg, #3b82f6, #1d4ed8); color: white;
      box-shadow: 0 2px 8px rgba(59,130,246,0.3);
    }
    .nav-item.active:hover { background: linear-gradient(135deg, #2563eb, #1e40af); }
    .nav-icon { font-size: 1.15rem; width: 24px; text-align: center; flex-shrink: 0; }
    .nav-label { font-weight: 500; font-size: 0.9rem; flex: 1; }
    .nav-badge {
      background: #ef4444; color: white; font-size: 0.7rem;
      padding: 2px 6px; border-radius: 10px; font-weight: 600;
    }
    .collapsed .nav-item { justify-content: center; padding: 10px; }
    .stat-card {
      padding: 10px 12px; background: var(--bg-surface-secondary);
      border-radius: 8px; margin-bottom: 6px;
    }
    .stat-label { font-size: 0.75rem; color: var(--text-muted); margin-bottom: 2px; }
    .stat-value { font-size: 1rem; font-weight: 700; color: var(--text-primary); }
    .stat-value.success { color: #10b981; }
    .sidebar-footer { margin-top: auto; padding: 12px; text-align: center; }
    .version { font-size: 0.7rem; color: var(--text-muted); }
    .backdrop { display: none; position: fixed; inset: 0; background: rgba(0,0,0,0.5); z-index: 899; }
    @media (max-width: 768px) {
      .sidebar { transform: translateX(-100%); width: 260px !important; box-shadow: 8px 0 24px rgba(0,0,0,0.2); }
      .sidebar.mobile-open { transform: translateX(0); }
      .sidebar.mobile-open .backdrop { display: block; }
    }
  `]
})
export class SidebarComponent {
  @Input() collapsed = false;
  mobileOpen = false;

  menu = [
    { icon: '🏠', label: 'Início', route: '/', exact: true },
    { icon: '💰', label: 'Benefícios', route: '/beneficios', badge: '3' },
    { icon: '🔄', label: 'Transferências', route: '/transferencias' },
    { icon: '📊', label: 'Relatórios', route: '/about' },
  ];

  openMobile(): void { this.mobileOpen = true; }
}
