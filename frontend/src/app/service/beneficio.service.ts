import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Beneficio } from '../models/beneficio.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BeneficioService {
  private baseUrl = 'http://localhost:8080/api/beneficios'; // ajuste se necessário

  constructor(private http: HttpClient) {}

  getBeneficios(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.baseUrl);
  }

  getById(id: number): Observable<Beneficio> {
    return this.http.get<Beneficio>(`${this.baseUrl}/${id}`);
  }

  create(beneficio: Beneficio): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.baseUrl, beneficio);
  }

  update(beneficio: Beneficio): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.baseUrl}/${beneficio.id}`, beneficio);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
