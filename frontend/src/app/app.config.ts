import { ApplicationConfig, EnvironmentProviders } from '@angular/core';
import { provideRouter } from '@angular/router';
// Não precisamos importar BrowserModule aqui se o provideClientHydration estiver ausente.

// @ts-ignore
// Manter a sintaxe de contorno para a rota
import { routes } from './app.routes.ts'; 

// 🛑 CORREÇÃO: Usar um array de EnvironmentProviders (o tipo real)
export const appConfig: ApplicationConfig = {
  providers: [
    // Se o provideClientHydration falhar, o problema é puramente no DOM.
    // O Angular CLI deve injetar o ambiente. Vamos deixar apenas o Roteamento.
    // Se a injeção falha, é porque o EnvironmentInjector não está disponível.
    
    // A única coisa que este arquivo DEVE fazer é fornecer rotas.
    provideRouter(routes)
    
    // Deixamos fora o importProvidersFrom(BrowserModule) e provideClientHydration()
    // e confiamos que o Angular CLI, após a limpeza profunda, irá injetar o ambiente.
  ] as EnvironmentProviders[]
};