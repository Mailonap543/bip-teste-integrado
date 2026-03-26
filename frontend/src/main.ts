import { bootstrapApplication } from '@angular/platform-browser';
import { AppComponent } from './app/app.component';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app/app.routes';
import { jwtInterceptor } from './app/interceptors/jwt.interceptor';
import { loadingInterceptor } from './app/shared/interceptors/loading.interceptor';
import { errorInterceptor } from './app/shared/interceptors/error.interceptor';

bootstrapApplication(AppComponent, {
  providers: [
    provideHttpClient(withInterceptors([jwtInterceptor, loadingInterceptor, errorInterceptor])),
    provideRouter(routes),
  ],
}).catch(err => console.error(err));
