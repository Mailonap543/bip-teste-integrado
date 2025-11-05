import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-about',
  standalone: true, 
  imports: [CommonModule],
  templateUrl: './about.component.html',
  styleUrls: ['./about.component.css']
})
// ✅ CORREÇÃO: Certifique-se de que o nome da classe é 'AboutComponent'
export class AboutComponent { 
    title = 'Sobre a Aplicação'; 
    version = '1.0.0';
}