import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common'; // Para diretivas básicas, se usadas
import { RouterOutlet } from '@angular/router'; //  ESSENCIAL para <router-outlet> (Resolve NG8001)


@Component({
  selector: 'app-root',
  standalone: true, 
  imports: [
    CommonModule,
    RouterOutlet // Adiciona o RouterOutlet ao componente
  ],
  templateUrl: './app.html', 
  styleUrls: ['./app.component.css'] 
})
export class App {
  // Você pode usar 'title' como propriedade normal, ou 'protected readonly title = signal('frontend');'
  // Depende se você usa a sintaxe de Signal no template. Vamos manter o seu Signal.
  protected readonly title = signal('frontend');
}