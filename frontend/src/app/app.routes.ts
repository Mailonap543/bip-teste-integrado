import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { BeneficioListComponent } from './components/beneficio-list/beneficio-list.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'beneficios', component: BeneficioListComponent },
  { path: '**', redirectTo: '' }
];

