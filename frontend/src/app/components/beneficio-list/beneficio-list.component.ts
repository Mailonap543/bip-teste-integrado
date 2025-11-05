import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BeneficioService } from '../../service/beneficio.service';
import { Beneficio } from '../../models/beneficio.model';
import { BeneficioFormComponent } from '../beneficio-form/beneficio-form.component';

@Component({
  selector: 'app-beneficio-list',
  standalone: true,
  imports: [CommonModule, BeneficioFormComponent],
  templateUrl: './beneficio-list.component.html',
  styleUrls: ['./beneficio-list.component.css']
})
export class BeneficioListComponent implements OnInit {
  beneficios: Beneficio[] = [];
  beneficioSelecionado: Beneficio | null = null;

  constructor(private beneficioService: BeneficioService) {}

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.beneficioService.getBeneficios().subscribe({
      next: (data) => this.beneficios = data ?? [],
      error: (err) => console.error('Erro ao carregar benefícios', err)
    });
  }

  novoBeneficio(): void {
    this.beneficioSelecionado = {
      id: undefined,
      nome: '',
      descricao: '',
      saldo: 0,
      ativa: true,
      valor: 0
    } as Beneficio;
  }

  editar(b: Beneficio): void {
    this.beneficioSelecionado = { ...b };
  }

  excluir(id?: number): void {
    if (id === undefined) return;
    if (!confirm(`Excluir benefício ${id}?`)) return;
    this.beneficioService.delete(id).subscribe({
      next: () => this.listar(),
      error: (err) => console.error('Erro ao excluir', err)
    });
  }

  salvarBeneficioEvent(b: Beneficio): void {
    if (b.id) {
      this.beneficioService.update(b).subscribe({
        next: () => { this.listar(); this.beneficioSelecionado = null; },
        error: (err) => console.error(err)
      });
    } else {
      this.beneficioService.create(b).subscribe({
        next: () => { this.listar(); this.beneficioSelecionado = null; },
        error: (err) => console.error(err)
      });
    }
  }

  cancelarEvent(): void {
    this.beneficioSelecionado = null;
  }
}
