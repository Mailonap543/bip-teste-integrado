import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./components/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./components/login/login.component').then(m => m.LoginComponent)
  },
  {
    path: 'beneficios',
    loadComponent: () => import('./components/beneficio-list/beneficio-list.component').then(m => m.BeneficioListComponent),
    canActivate: [authGuard]
  },
  {
    path: 'transferencias',
    loadComponent: () => import('./components/transferencia-form/transferencia-form.component').then(m => m.TransferenciaFormComponent),
    canActivate: [authGuard]
  },
  {
    path: 'about',
    loadComponent: () => import('./components/about/about.component').then(m => m.AboutComponent)
  },
  { path: '**', redirectTo: '' }
];
