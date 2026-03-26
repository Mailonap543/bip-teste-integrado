import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { BeneficioListComponent } from './components/beneficio-list/beneficio-list.component';
import { TransferenciaFormComponent } from './components/transferencia-form/transferencia-form.component';
import { AboutComponent } from './components/about/about.component';
import { LoginComponent } from './components/login/login.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'beneficios', component: BeneficioListComponent, canActivate: [authGuard] },
  { path: 'transferencias', component: TransferenciaFormComponent, canActivate: [authGuard] },
  { path: 'about', component: AboutComponent },
  { path: '**', redirectTo: '' }
];
