import { bootstrapApplication } from '@angular/platform-browser';
// ✅ CORREÇÃO: Altera o nome do componente e o caminho de importação.
import { AppComponent } from './app/app.component'; 
import { config } from './app/app.config.server'; // Presumindo que você tem um app.config.server

const bootstrap = () => bootstrapApplication(AppComponent, config);

export default bootstrap;