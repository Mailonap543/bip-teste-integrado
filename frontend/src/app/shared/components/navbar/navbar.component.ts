import { Component, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  template: `
    <nav class="navbar">
      <div class="navbar-left">
        <button class="menu-toggle" (click)="toggleSidebar.emit()">&#9776;</button>
        <a routerLink="/" class="logo">&#128176; Beneficios</a>
      </div>
      <div class="nav-links">
        <a routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">Inicio</a>
        <a routerLink="/beneficios" routerLinkActive="active">Beneficios</a>
        <a routerLink="/transferencias" routerLinkActive="active">Transferencias</a>
        <a routerLink="/about" routerLinkActive="active">Sobre</a>
      </div>
      <div class="nav-right">
        <button class="theme-btn" (click)="themeService.toggle()">{{ themeService.isDark() ? '☀' : '🌙' }}</button>
        <ng-container *ngIf="authService.isLoggedIn(); else loginBtn">
          <span class="user-name">{{ authService.getCurrentUser() }}</span>
          <button class="btn-logout" (click)="logout()">Sair</button>
        </ng-container>
        <ng-template #loginBtn>
          <a routerLink="/login" class="btn-login">Login</a>
        </ng-template>
      </div>
    </nav>
  `,
  styles: [`
    .navbar {
      display: flex; align-items: center; justify-content: space-between;
      height: 60px; padding: 0 20px;
      background: var(--bg-surface); border-bottom: 1px solid var(--border-color);
      box-shadow: var(--shadow-sm); position: sticky; top: 0; z-index: 1000;
    }
    .logo { font-weight: 700; font-size: 1.2rem; color: var(--text-primary); text-decoration: none; }
    .nav-links { display: flex; gap: 4px; }
    .nav-links a {
      padding: 8px 14px; border-radius: 6px; text-decoration: none;
      color: var(--text-secondary); font-weight: 500; transition: all 0.2s;
    }
    .nav-links a:hover { background: var(--bg-surface-secondary); color: var(--text-primary); text-decoration: none; }
    .nav-links a.active { background: var(--primary-50); color: var(--primary-600); }
    .nav-right { display: flex; align-items: center; gap: 10px; }
    .theme-btn { background: none; border: none; font-size: 1.2rem; cursor: pointer; }
    .user-name { color: var(--text-secondary); font-size: 0.9rem; }
    .btn-login {
      background: var(--primary-500); color: white; padding: 6px 16px;
      border-radius: 6px; text-decoration: none; font-weight: 500;
    }
    .btn-logout {
      background: var(--error-500); color: white; padding: 6px 16px;
      border: none; border-radius: 6px; cursor: pointer; font-weight: 500;
    }
    .menu-toggle { display: none; background: none; border: none; font-size: 1.5rem; cursor: pointer; }
    @media (max-width: 768px) {
      .nav-links { display: none; }
      .menu-toggle { display: block; }
    }
  `]
})
export class NavbarComponent {
  @Output() toggleSidebar = new EventEmitter<void>();

  constructor(
    public authService: AuthService,
    public themeService: ThemeService,
    private router: Router
  ) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
