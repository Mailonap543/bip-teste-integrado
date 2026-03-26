import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-empty-state',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="empty-state animate-fade-in">
      <div class="empty-icon">{{ icon }}</div>
      <h3 class="empty-title">{{ title }}</h3>
      <p class="empty-description">{{ description }}</p>
      <ng-content></ng-content>
    </div>
  `,
  styles: [`
    .empty-state {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 60px 20px;
      text-align: center;
    }
    .empty-icon {
      font-size: 4rem;
      margin-bottom: 16px;
      opacity: 0.8;
    }
    .empty-title {
      font-size: 1.25rem;
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 8px;
    }
    .empty-description {
      color: var(--text-secondary);
      font-size: 0.95rem;
      max-width: 400px;
    }
  `]
})
export class EmptyStateComponent {
  @Input() icon = '';
  @Input() title = 'Nenhum item encontrado';
  @Input() description = '';
}
