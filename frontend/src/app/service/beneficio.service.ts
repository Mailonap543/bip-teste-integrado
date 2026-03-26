import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Beneficio } from '../models/beneficio.model';
import { ApiResponse } from '../models/api.model';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private baseUrl = 'http://localhost:8080/api/v1/beneficios';

  constructor(private http: HttpClient) {}

  getBeneficios(): Observable<Beneficio[]> {
    return this.http.get<ApiResponse<Beneficio[]>>(this.baseUrl)
      .pipe(map(response => response.data ?? []));
  }

  getById(id: number): Observable<Beneficio> {
    return this.http.get<ApiResponse<Beneficio>>(`${this.baseUrl}/${id}`)
      .pipe(map(response => response.data));
  }

  create(beneficio: Partial<Beneficio>): Observable<Beneficio> {
    return this.http.post<ApiResponse<Beneficio>>(this.baseUrl, {
      nome: beneficio.titular,
      saldo: beneficio.saldo,
      ativa: beneficio.ativa
    }).pipe(map(response => response.data));
  }

  update(beneficio: Beneficio): Observable<Beneficio> {
    return this.http.put<ApiResponse<Beneficio>>(`${this.baseUrl}/${beneficio.id}`, {
      nome: beneficio.titular,
      saldo: beneficio.saldo,
      ativa: beneficio.ativa
    }).pipe(map(response => response.data));
  }

  delete(id: number): Observable<void> {
    return this.http.delete<ApiResponse<void>>(`${this.baseUrl}/${id}`)
      .pipe(map(() => undefined));
  }

  transfer(origemId: number, destinoId: number, valor: number): Observable<void> {
    return this.http.post<ApiResponse<void>>(
      `${this.baseUrl}/transfer/${origemId}/${destinoId}/${valor}`, {}
    ).pipe(map(() => undefined));
  }
}
