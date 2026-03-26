import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  template: `
    <div class="home-page">
      <section class="hero">
        <h1>Sistema de Beneficios</h1>
        <p>Gerencie seus beneficios e realize transferencias de forma simples e segura.</p>
        <div class="hero-actions">
          <a routerLink="/beneficios" class="btn-hero primary">Gerenciar Beneficios</a>
          <a routerLink="/transferencias" class="btn-hero secondary">Transferencias</a>
        </div>
      </section>
      <section class="features">
        <div class="feature-card" *ngFor="let f of features">
          <div class="feature-icon">{{ f.icon }}</div>
          <h3>{{ f.title }}</h3>
          <p>{{ f.desc }}</p>
        </div>
      </section>
    </div>
  `,
  styles: [`
    .home-page { max-width: 1000px; margin: 0 auto; }
    .hero {
      text-align: center; padding: 60px 20px;
      background: linear-gradient(135deg, var(--primary-500), var(--secondary-500));
      border-radius: 12px; color: white; margin-bottom: 40px;
    }
    .hero h1 { font-size: 2.5rem; margin-bottom: 12px; }
    .hero p { font-size: 1.1rem; opacity: 0.9; margin-bottom: 28px; }
    .hero-actions { display: flex; gap: 16px; justify-content: center; flex-wrap: wrap; }
    .btn-hero {
      padding: 12px 28px; border-radius: 8px; text-decoration: none;
      font-weight: 600; font-size: 1rem; color: white; transition: transform 0.2s;
    }
    .btn-hero:hover { transform: translateY(-2px); text-decoration: none; }
    .btn-hero.primary { background: var(--success-500); }
    .btn-hero.secondary { background: var(--warning-500); }
    .features { display: grid; grid-template-columns: repeat(auto-fit, minmax(260px, 1fr)); gap: 20px; }
    .feature-card {
      background: var(--bg-surface); padding: 28px; border-radius: 10px;
      box-shadow: var(--shadow-sm); text-align: center; transition: transform 0.2s;
    }
    .feature-card:hover { transform: translateY(-4px); box-shadow: var(--shadow-md); }
    .feature-icon { font-size: 2.5rem; margin-bottom: 12px; }
    .feature-card h3 { margin: 0 0 8px; color: var(--text-primary); }
    .feature-card p { margin: 0; color: var(--text-secondary); font-size: 0.95rem; }
  `]
})
export class HomeComponent {
  features = [
    { icon: '💰', title: 'Gerenciar Beneficios', desc: 'Cadastre, edite e exclua beneficios com facilidade.' },
    { icon: '🔄', title: 'Transferencias', desc: 'Transfira valores entre beneficios de forma segura.' },
    { icon: '📊', title: 'Controle Total', desc: 'Acompanhe saldos e historico de transferencias.' }
  ];
}
