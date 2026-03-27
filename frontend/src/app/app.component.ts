import { Component, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/components/navbar/navbar.component';
import { SidebarComponent } from './shared/components/sidebar/sidebar.component';
import { FooterComponent } from './shared/components/footer/footer.component';
import { LoadingSpinnerComponent } from './shared/components/loading-spinner/loading-spinner.component';
import { ToastContainerComponent } from './shared/components/toast/toast.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, NavbarComponent, SidebarComponent, FooterComponent, LoadingSpinnerComponent, ToastContainerComponent],
  template: `
    <div class="app-layout">
      <app-navbar (toggleSidebar)="onToggleSidebar()"></app-navbar>
      <app-sidebar #sidebar [collapsed]="sidebarCollapsed"></app-sidebar>
      <main class="app-main" [class.collapsed]="sidebarCollapsed">
        <div class="app-content">
          <router-outlet></router-outlet>
        </div>
        <app-footer></app-footer>
      </main>
      <app-loading-spinner></app-loading-spinner>
      <app-toast-container></app-toast-container>
    </div>
  `,
  styles: [`
    .app-layout { display: flex; flex-direction: column; min-height: 100vh; }
    .app-main {
      margin-left: 220px; margin-top: 60px; min-height: calc(100vh - 60px);
      display: flex; flex-direction: column; transition: margin-left 0.25s ease;
    }
    .app-main.collapsed { margin-left: 56px; }
    .app-content {
      flex: 1; padding: 24px; max-width: 1400px; width: 100%; margin: 0 auto;
      animation: fadeInUp 0.3s ease-out;
    }
    @keyframes fadeInUp {
      from { opacity: 0; transform: translateY(12px); }
      to { opacity: 1; transform: translateY(0); }
    }
    @media (max-width: 768px) {
      .app-main, .app-main.collapsed { margin-left: 0; }
      .app-content { padding: 16px; }
    }
  `]
})
export class AppComponent {
  sidebarCollapsed = false;
  @ViewChild('sidebar') sidebar!: SidebarComponent;

  onToggleSidebar(): void {
    if (window.innerWidth <= 768) {
      this.sidebar?.openMobile();
    } else {
      this.sidebarCollapsed = !this.sidebarCollapsed;
    }
  }
}
