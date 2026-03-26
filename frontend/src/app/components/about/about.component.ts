import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-about',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="page">
      <h1>Sobre o Sistema</h1>
      <p class="version">Versao 1.0.0</p>
      <section class="section">
        <h2>Tecnologias</h2>
        <div class="tech-grid">
          <div class="tech-card" *ngFor="let t of tech">
            <div class="tech-icon">{{ t.icon }}</div>
            <strong>{{ t.name }}</strong>
            <span>{{ t.desc }}</span>
          </div>
        </div>
      </section>
      <section class="section">
        <h2>Funcionalidades</h2>
        <ul>
          <li *ngFor="let f of features">&#10003; {{ f }}</li>
        </ul>
      </section>
      <a routerLink="/" class="btn-back">&#127968; Voltar para Home</a>
    </div>
  `,
  styles: [`
    h1 { margin: 0; font-size: 1.75rem; font-weight: 700; }
    .version { color: var(--text-secondary); margin: 4px 0 24px; }
    .section { margin-bottom: 28px; }
    .section h2 { font-size: 1.25rem; margin-bottom: 12px; }
    .tech-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(180px, 1fr)); gap: 14px; }
    .tech-card {
      background: var(--bg-surface); padding: 18px; border-radius: 8px;
      box-shadow: var(--shadow-sm); text-align: center; display: flex; flex-direction: column; gap: 4px;
    }
    .tech-icon { font-size: 2rem; }
    .tech-card strong { color: var(--text-primary); }
    .tech-card span { color: var(--text-secondary); font-size: 0.85rem; }
    ul { list-style: none; padding: 0; }
    li { padding: 6px 0; color: var(--text-secondary); }
    .btn-back {
      display: inline-block; background: var(--primary-500); color: white;
      padding: 10px 20px; border-radius: 6px; text-decoration: none; font-weight: 600;
    }
  `]
})
export class AboutComponent {
  tech = [
    { icon: '', name: 'Angular 20', desc: 'Frontend' },
    { icon: '', name: 'Spring Boot 3.2', desc: 'Backend' },
    { icon: '', name: 'PostgreSQL', desc: 'Database' },
    { icon: '', name: 'JWT', desc: 'Security' },
    { icon: '', name: 'Swagger', desc: 'API Docs' },
    { icon: '', name: 'Docker', desc: 'Container' }
  ];
  features = ['CRUD de beneficios', 'Transferencias seguras', 'JWT Auth', 'API v1', 'Swagger', 'Dark mode', 'Responsivo'];
}
