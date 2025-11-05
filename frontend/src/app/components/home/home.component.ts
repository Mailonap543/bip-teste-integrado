import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { BeneficioListComponent } from '../beneficio-list/beneficio-list.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterModule, BeneficioListComponent],
  template: `
    <div style="padding: 40px; text-align: center; background-color: #f8f9fa;">
      <h1 style="color: #007bff;">Bem-vindo ao Sistema de Benefícios</h1>
      <p>
        Gerencie seus benefícios abaixo ou
        <a routerLink="/beneficios" style="color: #007bff; text-decoration: none;">
          acesse a página dedicada
        </a>.
      </p>
    </div>

    <section style="margin: 40px auto; max-width: 1000px;">
      <app-beneficio-list></app-beneficio-list>
    </section>
  `
})
export class HomeComponent { } 
