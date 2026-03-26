import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { BeneficioListComponent } from './components/beneficio-list/beneficio-list.component';
import { TransferenciaFormComponent } from './components/transferencia-form/transferencia-form.component';
import { AboutComponent } from './components/about/about.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'beneficios', component: BeneficioListComponent },
  { path: 'transferencias', component: TransferenciaFormComponent },
  { path: 'about', component: AboutComponent },
  { path: '**', redirectTo: '' }
];
