import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// ⚠️ Você deve importar todos os componentes que estão sendo usados nas rotas
// Presumo que você tenha corrigido os caminhos e o problema Standalone.
import { HomeComponent } from './components/home/home.component';
import { AboutComponent } from './components/about/about.component';
import { BeneficioListComponent } from './components/beneficio-list/beneficio-list.component';
import { BeneficioFormComponent } from './components/beneficio-form/beneficio-form.component';

// ✅ Definição das Rotas
const routes: Routes = [
  // Rota raiz (página inicial)
  { path: '', component: HomeComponent },
  
  // Rota para a página "Sobre"
  { path: 'about', component: AboutComponent },
  
  // Rota para a lista de benefícios
  { path: 'beneficios', component: BeneficioListComponent },
  
  // Rota para criar/editar um benefício (exemplo com ID opcional)
  { path: 'beneficios/novo', component: BeneficioFormComponent },
  { path: 'beneficios/editar/:id', component: BeneficioFormComponent },
  
  // Rota Wildcard (para URLs não encontradas - 404)
  // ⚠️ Você precisará criar o componente 'NotFoundComponent' se for usá-lo.
  // { path: '**', component: NotFoundComponent } 
];

@NgModule({
  // Configura o roteamento no nível raiz da aplicação
  imports: [RouterModule.forRoot(routes)],
  // Exporta o RouterModule para que o AppModule possa importá-lo
  exports: [RouterModule]
})
export class AppRoutingModule { }