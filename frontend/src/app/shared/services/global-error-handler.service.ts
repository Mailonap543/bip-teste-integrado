import { ErrorHandler, Injectable, Injector } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NotificationService } from './notification.service';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

  constructor(private injector: Injector) {}

  handleError(error: any): void {
    try {
      const notification = this.injector.get(NotificationService);
      const message = error instanceof HttpErrorResponse
        ? (error.error?.message || error.message || 'Erro de conexao')
        : (error?.message || 'Erro inesperado na aplicacao');
      notification.error(message);
    } catch {
      console.error('Application Error:', error);
    }
  }
}
