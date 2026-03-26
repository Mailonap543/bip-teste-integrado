import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../services/notification.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notificationService = inject(NotificationService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let message = 'Erro inesperado';
      if (error.error?.message) message = error.error.message;
      else if (error.status === 0) message = 'Sem conexao com o servidor';
      else if (error.status === 401) message = 'Sessao expirada';
      else if (error.status === 403) message = 'Acesso negado';
      else if (error.status === 404) message = 'Recurso nao encontrado';
      else if (error.status >= 500) message = 'Erro interno do servidor';

      notificationService.error(message);
      return throwError(() => error);
    })
  );
};
