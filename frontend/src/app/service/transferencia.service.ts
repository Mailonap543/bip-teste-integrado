import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { Transferencia } from '../models/transferencia.model';
import { ApiResponse } from '../models/api.model';

@Injectable({
  providedIn: 'root'
})
export class TransferenciaService {

  private apiUrl = 'http://localhost:8080/api/v1/transferencias';

  constructor(private http: HttpClient) { }

  transferir(fromId: number, toId: number, amount: number): Observable<string> {
    const body: Transferencia = { fromId, toId, amount };
    return this.http.post<ApiResponse<string>>(this.apiUrl, body)
      .pipe(map(response => response.message));
  }
}
