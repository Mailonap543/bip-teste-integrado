import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoadingService } from '../../services/loading.service';

@Component({
  selector: 'app-loading-spinner',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="loading-overlay" *ngIf="loadingService.isLoading()">
      <div class="spinner-box">
        <div class="spinner"></div>
        <p>Carregando...</p>
      </div>
    </div>
  `,
  styles: [`
    .loading-overlay {
      position: fixed; top: 0; left: 0; right: 0; bottom: 0;
      background: rgba(0,0,0,0.3); backdrop-filter: blur(4px);
      display: flex; align-items: center; justify-content: center; z-index: 9999;
    }
    .spinner-box {
      background: var(--bg-surface); padding: 32px 48px;
      border-radius: 16px; box-shadow: var(--shadow-lg);
      display: flex; flex-direction: column; align-items: center; gap: 16px;
    }
    .spinner {
      width: 48px; height: 48px;
      border: 4px solid var(--border-color);
      border-top-color: var(--primary-500);
      border-radius: 50%;
      animation: spin 0.8s linear infinite;
    }
    p { color: var(--text-secondary); margin: 0; }
    @keyframes spin { to { transform: rotate(360deg); } }
  `]
})
export class LoadingSpinnerComponent {
  constructor(public loadingService: LoadingService) {}
}
