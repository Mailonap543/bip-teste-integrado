import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransferenciaService {

  private apiUrl = 'http://localhost:8080/api/transferencias';

  constructor(private http: HttpClient) { }

  transferir(fromId: number, toId: number, valor: number): Observable<any> {
    let params = new HttpParams()
      .set('fromId', fromId)
      .set('toId', toId)
      .set('valor', valor);
    return this.http.post(this.apiUrl, {}, { params });
  }
}
