import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Beneficio } from '../models/beneficio.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private baseUrl = 'http://localhost:8080/api/beneficios';

  constructor(private http: HttpClient) {}

  getBeneficios(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.baseUrl);
  }

  getById(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.baseUrl}/${id}`);
  }

  create(beneficio: Partial<Beneficio>): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.baseUrl, {
      nome: beneficio.titular,
      saldo: beneficio.saldo,
      ativa: beneficio.ativa
    });
  }

  update(beneficio: Beneficio): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.baseUrl}/${beneficio.id}`, {
      nome: beneficio.titular,
      saldo: beneficio.saldo,
      ativa: beneficio.ativa
    });
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  transfer(origemId: number, destinoId: number, valor: number): Observable<void> {
    return this.http.post<void>(
      `${this.baseUrl}/transfer/${origemId}/${destinoId}/${valor}`,
      {}
    );
  }
}
