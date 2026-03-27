import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  template: `
    <nav class="navbar">
      <div class="nav-left">
        <button class="hamburger" (click)="toggleSidebar.emit()">&#9776;</button>
        <a routerLink="/" class="brand">
          <span class="brand-icon">&#128176;</span>
          <span class="brand-text">BIP<span class="brand-accent">Benefícios</span></span>
        </a>
      </div>

      <div class="nav-search desktop-only">
        <span class="search-icon">&#128269;</span>
        <input type="text" placeholder="Buscar benefícios, transferências..." [(ngModel)]="searchQuery" />
      </div>

      <div class="nav-right">
        <button class="icon-btn" (click)="themeService.toggle()" [title]="themeService.isDark() ? 'Modo claro' : 'Modo escuro'">
          {{ themeService.isDark() ? '☀️' : '🌙' }}
        </button>

        <div class="notif-wrapper">
          <button class="icon-btn" (click)="showNotifs = !showNotifs">
            🔔
            <span class="badge" *ngIf="notifCount > 0">{{ notifCount }}</span>
          </button>
          <div class="notif-dropdown" *ngIf="showNotifs">
            <div class="notif-header">Notificações</div>
            <div class="notif-item" *ngFor="let n of notifications">
              <span class="notif-icon">{{ n.icon }}</span>
              <div class="notif-content">
                <span class="notif-text">{{ n.text }}</span>
                <span class="notif-time">{{ n.time }}</span>
              </div>
            </div>
            <div class="notif-empty" *ngIf="notifications.length === 0">Sem notificações</div>
          </div>
        </div>

        <div class="user-wrapper">
          <button class="user-btn" *ngIf="authService.isLoggedIn()" (click)="showUserMenu = !showUserMenu">
            <span class="avatar">{{ getInitials() }}</span>
            <span class="user-name desktop-only">{{ authService.getCurrentUser() }}</span>
            <span class="arrow desktop-only">▼</span>
          </button>
          <div class="user-dropdown" *ngIf="showUserMenu">
            <div class="user-info">
              <span class="avatar-lg">{{ getInitials() }}</span>
              <div>
                <div class="user-fullname">{{ authService.getCurrentUser() }}</div>
                <div class="user-role">Usuário</div>
              </div>
            </div>
            <div class="dropdown-divider"></div>
            <a class="dropdown-item" routerLink="/about" (click)="showUserMenu = false">ℹ️ Sobre</a>
            <div class="dropdown-divider"></div>
            <button class="dropdown-item logout" (click)="logout()">🚪 Sair</button>
          </div>
          <a routerLink="/login" class="login-btn" *ngIf="!authService.isLoggedIn()">🔐 Entrar</a>
        </div>
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      display: flex; align-items: center; justify-content: space-between;
      height: 64px; padding: 0 20px; gap: 16px;
      background: linear-gradient(135deg, #1e40af, #3b82f6);
      color: white; position: sticky; top: 0; z-index: 1000;
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
    .nav-left { display: flex; align-items: center; gap: 12px; }
    .hamburger {
      background: none; border: none; color: white; font-size: 1.4rem;
      cursor: pointer; padding: 6px; border-radius: 6px; display: none;
    }
    .hamburger:hover { background: rgba(255,255,255,0.15); }
    .brand { display: flex; align-items: center; gap: 8px; text-decoration: none; color: white; }
    .brand-icon { font-size: 1.6rem; }
    .brand-text { font-size: 1.3rem; font-weight: 700; letter-spacing: -0.5px; }
    .brand-accent { font-weight: 400; opacity: 0.85; margin-left: 4px; }
    .nav-search {
      flex: 1; max-width: 420px; position: relative;
    }
    .nav-search input {
      width: 100%; padding: 9px 14px 9px 36px;
      border: none; border-radius: 8px; font-size: 0.9rem;
      background: rgba(255,255,255,0.15); color: white;
      transition: background 0.2s;
    }
    .nav-search input::placeholder { color: rgba(255,255,255,0.6); }
    .nav-search input:focus { outline: none; background: rgba(255,255,255,0.25); }
    .search-icon { position: absolute; left: 10px; top: 50%; transform: translateY(-50%); font-size: 0.9rem; opacity: 0.7; }
    .nav-right { display: flex; align-items: center; gap: 8px; }
    .icon-btn {
      background: rgba(255,255,255,0.1); border: none; color: white;
      width: 40px; height: 40px; border-radius: 10px; cursor: pointer;
      font-size: 1.1rem; position: relative; transition: background 0.2s;
      display: flex; align-items: center; justify-content: center;
    }
    .icon-btn:hover { background: rgba(255,255,255,0.2); }
    .badge {
      position: absolute; top: 2px; right: 2px;
      background: #ef4444; color: white; font-size: 0.65rem;
      width: 18px; height: 18px; border-radius: 50%;
      display: flex; align-items: center; justify-content: center; font-weight: 700;
    }
    .notif-wrapper, .user-wrapper { position: relative; }
    .notif-dropdown, .user-dropdown {
      position: absolute; top: 100%; right: 0; margin-top: 8px;
      background: white; color: #1f2937; border-radius: 12px;
      box-shadow: 0 8px 30px rgba(0,0,0,0.15); min-width: 300px;
      z-index: 1001; animation: dropIn 0.2s ease;
    }
    @keyframes dropIn { from { opacity: 0; transform: translateY(-8px); } to { opacity: 1; transform: translateY(0); } }
    .notif-header { padding: 14px 16px; font-weight: 600; border-bottom: 1px solid #e5e7eb; }
    .notif-item { display: flex; gap: 10px; padding: 12px 16px; border-bottom: 1px solid #f3f4f6; cursor: pointer; transition: background 0.15s; }
    .notif-item:hover { background: #f9fafb; }
    .notif-icon { font-size: 1.2rem; }
    .notif-text { font-size: 0.85rem; display: block; }
    .notif-time { font-size: 0.75rem; color: #9ca3af; }
    .notif-empty { padding: 20px; text-align: center; color: #9ca3af; }
    .user-btn {
      display: flex; align-items: center; gap: 8px;
      background: rgba(255,255,255,0.1); border: none; color: white;
      padding: 6px 12px 6px 6px; border-radius: 10px; cursor: pointer; transition: background 0.2s;
    }
    .user-btn:hover { background: rgba(255,255,255,0.2); }
    .avatar {
      width: 32px; height: 32px; border-radius: 8px;
      background: rgba(255,255,255,0.25); display: flex;
      align-items: center; justify-content: center;
      font-weight: 700; font-size: 0.85rem;
    }
    .avatar-lg {
      width: 40px; height: 40px; border-radius: 10px;
      background: #3b82f6; color: white; display: flex;
      align-items: center; justify-content: center;
      font-weight: 700; font-size: 1rem;
    }
    .user-name { font-size: 0.9rem; font-weight: 500; }
    .arrow { font-size: 0.6rem; opacity: 0.7; }
    .user-info { display: flex; align-items: center; gap: 12px; padding: 16px; }
    .user-fullname { font-weight: 600; }
    .user-role { font-size: 0.8rem; color: #9ca3af; }
    .dropdown-divider { height: 1px; background: #e5e7eb; }
    .dropdown-item {
      display: flex; align-items: center; gap: 8px; padding: 12px 16px;
      color: #374151; text-decoration: none; cursor: pointer;
      border: none; background: none; width: 100%; font-size: 0.9rem;
      transition: background 0.15s;
    }
    .dropdown-item:hover { background: #f3f4f6; text-decoration: none; }
    .dropdown-item.logout { color: #ef4444; }
    .login-btn {
      background: white; color: #1e40af; padding: 8px 18px;
      border-radius: 8px; text-decoration: none; font-weight: 600;
      font-size: 0.9rem; transition: transform 0.2s;
    }
    .login-btn:hover { transform: scale(1.03); text-decoration: none; }
    .desktop-only { display: inline; }
    @media (max-width: 768px) {
      .hamburger { display: flex; }
      .desktop-only { display: none; }
      .nav-search { display: none; }
    }
  `]
})
export class NavbarComponent {
  @Output() toggleSidebar = new EventEmitter<void>();
  showNotifs = false;
  showUserMenu = false;
  searchQuery = '';
  notifCount = 2;
  notifications = [
    { icon: '💰', text: 'Benefício Maria Silva atualizado', time: 'há 5 min' },
    { icon: '🔄', text: 'Transferência de R$ 300 concluída', time: 'há 2 horas' },
  ];

  constructor(public authService: AuthService, public themeService: ThemeService, private router: Router) {}

  getInitials(): string {
    const name = this.authService.getCurrentUser() || 'U';
    return name.substring(0, 2).toUpperCase();
  }

  logout(): void {
    this.authService.logout();
    this.showUserMenu = false;
    this.router.navigate(['/login']);
  }
}
