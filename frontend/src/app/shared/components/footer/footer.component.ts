import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule],
  template: `
    <footer class="footer">
      <p>&copy; 2025 Sistema de Beneficios - Desafio Fullstack Integrado</p>
    </footer>
  `,
  styles: [`
    .footer {
      background: var(--bg-surface); border-top: 1px solid var(--border-color);
      padding: 14px 24px; text-align: center;
    }
    .footer p { margin: 0; color: var(--text-secondary); font-size: 0.85rem; }
  `]
})
export class FooterComponent {}
