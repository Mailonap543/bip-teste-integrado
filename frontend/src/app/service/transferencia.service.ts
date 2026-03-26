import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transferencia } from '../models/transferencia.model';

@Injectable({
  providedIn: 'root'
})
export class TransferenciaService {

  private apiUrl = 'http://localhost:8080/api/transferencias';

  constructor(private http: HttpClient) { }

  transferir(fromId: number, toId: number, amount: number): Observable<string> {
    const body: Transferencia = { fromId, toId, amount };
    return this.http.post(this.apiUrl, body, { responseType: 'text' });
  }
}
