import { Component, EventEmitter, Input, Output, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-confirm-dialog',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="dialog-backdrop" *ngIf="visible" (click)="onCancel()">
      <div class="dialog-card" (click)="$event.stopPropagation()">
        <div class="dialog-header">
          <span class="dialog-icon">{{ icon }}</span>
          <h3>{{ title }}</h3>
        </div>
        <p class="dialog-message">{{ message }}</p>
        <div class="dialog-actions">
          <button class="btn-cancel" (click)="onCancel()">{{ cancelText }}</button>
          <button class="btn-confirm" [class.danger]="type === 'danger'" (click)="onConfirm()">{{ confirmText }}</button>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .dialog-backdrop {
      position: fixed; top: 0; left: 0; right: 0; bottom: 0;
      background: rgba(0,0,0,0.5); display: flex; align-items: center;
      justify-content: center; z-index: 1000;
      animation: fadeIn 0.2s ease;
    }
    @keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
    .dialog-card {
      background: var(--bg-surface); border-radius: 12px; padding: 24px;
      max-width: 400px; width: 90%; box-shadow: 0 20px 60px rgba(0,0,0,0.3);
      animation: scaleIn 0.2s ease;
    }
    @keyframes scaleIn { from { transform: scale(0.9); opacity: 0; } to { transform: scale(1); opacity: 1; } }
    .dialog-header { display: flex; align-items: center; gap: 12px; margin-bottom: 12px; }
    .dialog-icon { font-size: 1.5rem; }
    .dialog-header h3 { margin: 0; font-size: 1.1rem; }
    .dialog-message { color: var(--text-secondary); margin: 0 0 20px; line-height: 1.5; }
    .dialog-actions { display: flex; gap: 10px; justify-content: flex-end; }
    .btn-cancel, .btn-confirm {
      padding: 8px 20px; border: none; border-radius: 6px;
      font-weight: 600; cursor: pointer; transition: all 0.2s;
    }
    .btn-cancel { background: var(--bg-surface-secondary); color: var(--text-primary); }
    .btn-cancel:hover { background: var(--divider); }
    .btn-confirm { background: var(--primary-500); color: white; }
    .btn-confirm:hover { transform: translateY(-1px); }
    .btn-confirm.danger { background: var(--error-500); }
    .btn-confirm.danger:hover { background: var(--error-700); }
  `]
})
export class ConfirmDialogComponent {
  @Input() visible = false;
  @Input() title = 'Confirmar';
  @Input() message = 'Tem certeza?';
  @Input() confirmText = 'Confirmar';
  @Input() cancelText = 'Cancelar';
  @Input() type: 'info' | 'danger' = 'info';
  @Input() icon = '?';

  @Output() confirmed = new EventEmitter<void>();
  @Output() cancelled = new EventEmitter<void>();

  onConfirm(): void { this.confirmed.emit(); }
  onCancel(): void { this.cancelled.emit(); }
}
