// Código Corrigido: src/app/app.component.ts

import { Component } from '@angular/core';
// 🛑 CORREÇÃO: Importamos o RouterOutlet e o RouterLink (e o CommonModule)
import { RouterOutlet, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common'; // O RouterLink depende disto

@Component({
  selector: 'app-root',
  standalone: true,
  // 🛑 CORREÇÃO: Usamos RouterOutlet e RouterLink para o componente Standalone
  imports: [CommonModule, RouterOutlet, RouterLink], 
  template: `
    <header>
      <nav style="padding: 10px; background-color: #f0f0f0;">
        <a routerLink="/" style="margin-right: 10px;">🏠 Início</a>
        <a routerLink="/about" style="margin-right: 10px;">ℹ️ Sobre</a>
        <a routerLink="/beneficios">💰 Benefícios</a>
      </nav>
      <hr>
    </header>

    <main style="padding: 20px;">
      <router-outlet></router-outlet>
    </main>

    <footer style="text-align: center; padding: 10px; background-color: #f0f0f0;">
      <hr>
      <p>&copy; 2025 Sistema de Benefícios</p>
    </footer>
  `
})
export class AppComponent {
  
}