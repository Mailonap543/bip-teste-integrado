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
      <nav class="sidebar-nav">
        <a *ngFor="let item of menu" [routerLink]="item.route" routerLinkActive="active"
           [routerLinkActiveOptions]="{exact: item.exact || false}" class="sidebar-item" (click)="mobileOpen = false">
          <span class="icon">{{ item.icon }}</span>
          <span class="label" *ngIf="!collapsed">{{ item.label }}</span>
        </a>
      </nav>
    </aside>
  `,
  styles: [`
    .sidebar {
      position: fixed; top: 60px; left: 0; bottom: 0; width: 220px;
      background: var(--bg-surface); border-right: 1px solid var(--border-color);
      transition: width 0.25s ease; z-index: 900; overflow-x: hidden;
    }
    .sidebar.collapsed { width: 56px; }
    .sidebar-nav { padding: 16px 8px; display: flex; flex-direction: column; gap: 4px; }
    .sidebar-item {
      display: flex; align-items: center; gap: 12px; padding: 10px 14px;
      border-radius: 6px; color: var(--text-secondary); text-decoration: none;
      transition: all 0.15s; min-height: 40px;
    }
    .sidebar-item:hover { background: var(--bg-surface-secondary); color: var(--text-primary); text-decoration: none; }
    .sidebar-item.active { background: var(--primary-50); color: var(--primary-600); }
    .icon { font-size: 1.1rem; }
    .collapsed .sidebar-item { justify-content: center; padding: 10px; }
    .backdrop { display: none; position: fixed; inset: 0; background: rgba(0,0,0,0.5); z-index: 899; }
    @media (max-width: 768px) {
      .sidebar { transform: translateX(-100%); width: 220px !important; }
      .sidebar.mobile-open { transform: translateX(0); }
      .sidebar.mobile-open .backdrop { display: block; }
    }
  `]
})
export class SidebarComponent {
  @Input() collapsed = false;
  mobileOpen = false;

  menu = [
    { icon: '🏠', label: 'Inicio', route: '/', exact: true },
    { icon: '💰', label: 'Beneficios', route: '/beneficios' },
    { icon: '🔄', label: 'Transferencias', route: '/transferencias' },
    { icon: 'ℹ️', label: 'Sobre', route: '/about' },
  ];

  openMobile(): void { this.mobileOpen = true; }
}
