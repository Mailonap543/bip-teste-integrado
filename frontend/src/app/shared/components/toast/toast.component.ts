import { Component, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService, Notification } from '../../services/notification.service';

@Component({
  selector: 'app-toast-container',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="toast-container">
      <div *ngFor="let n of notificationService.notifications()"
           class="toast"
           [class.success]="n.type === 'success'"
           [class.error]="n.type === 'error'"
           [class.warning]="n.type === 'warning'"
           [class.info]="n.type === 'info'">
        <span class="toast-icon">{{ getIcon(n.type) }}</span>
        <span class="toast-message">{{ n.message }}</span>
        <button *ngIf="n.undoAction" class="toast-undo" (click)="notificationService.undo(n.id)">Desfazer</button>
        <button class="toast-close" (click)="notificationService.dismiss(n.id)">&times;</button>
        <div class="toast-progress"></div>
      </div>
    </div>
  `,
  styles: [`
    .toast-container {
      position: fixed; top: 20px; right: 20px; z-index: 99999;
      display: flex; flex-direction: column; gap: 8px; max-width: 400px;
    }
    .toast {
      display: flex; align-items: center; gap: 10px; padding: 12px 16px;
      border-radius: 12px; color: white; font-weight: 500; font-size: 0.9rem;
      box-shadow: 0 8px 24px rgba(0,0,0,0.25);
      animation: slideInRight 0.4s cubic-bezier(0.4, 0, 0.2, 1);
      position: relative; overflow: hidden;
      backdrop-filter: blur(10px);
    }
    .toast.success { background: rgba(16, 185, 129, 0.9); }
    .toast.error { background: rgba(239, 68, 68, 0.9); }
    .toast.warning { background: rgba(245, 158, 11, 0.9); }
    .toast.info { background: rgba(59, 130, 246, 0.9); }
    .toast-icon { font-size: 1.1rem; }
    .toast-message { flex: 1; }
    .toast-undo {
      background: rgba(255,255,255,0.2); border: none; color: white;
      padding: 4px 10px; border-radius: 4px; cursor: pointer; font-weight: 600;
      transition: background 0.2s;
    }
    .toast-undo:hover { background: rgba(255,255,255,0.3); }
    .toast-close {
      background: none; border: none; color: white; cursor: pointer;
      font-size: 1.2rem; padding: 0 4px; opacity: 0.7;
    }
    .toast-close:hover { opacity: 1; }
    .toast-progress {
      position: absolute; bottom: 0; left: 0; height: 3px;
      background: rgba(255,255,255,0.4); width: 100%;
      animation: shrink 3s linear forwards;
    }
    @keyframes slideInRight { from { transform: translateX(120%); opacity: 0; } to { transform: translateX(0); opacity: 1; } }
    @keyframes shrink { from { width: 100%; } to { width: 0%; } }
  `]
})
export class ToastContainerComponent {
  constructor(public notificationService: NotificationService) {}

  getIcon(type: string): string {
    switch (type) {
      case 'success': return '✓';
      case 'error': return '✕';
      case 'warning': return '⚠';
      case 'info': return 'ℹ';
      default: return '';
    }
  }
}
