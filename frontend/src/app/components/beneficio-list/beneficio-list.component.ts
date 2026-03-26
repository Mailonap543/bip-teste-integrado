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
  carregando = false;
  mensagem = '';
  tipoMensagem = '';

  constructor(private beneficioService: BeneficioService) {}

  ngOnInit(): void {
    this.listar();
  }

  listar(): void {
    this.carregando = true;
    this.beneficioService.getBeneficios().subscribe({
      next: (data) => {
        this.beneficios = data ?? [];
        this.carregando = false;
      },
      error: (err) => {
        console.error('Erro ao carregar benefícios', err);
        this.mostrarMensagem('Erro ao carregar benefícios', 'erro');
        this.carregando = false;
      }
    });
  }

  novoBeneficio(): void {
    this.beneficioSelecionado = {
      id: undefined,
      titular: '',
      saldo: 0,
      ativa: true
    } as Beneficio;
  }

  editar(b: Beneficio): void {
    this.beneficioSelecionado = { ...b };
  }

  excluir(id?: number): void {
    if (id === undefined) return;
    if (!confirm('Deseja realmente excluir este benefício?')) return;
    this.beneficioService.delete(id).subscribe({
      next: () => {
        this.mostrarMensagem('Benefício excluído com sucesso', 'sucesso');
        this.listar();
      },
      error: (err) => {
        console.error('Erro ao excluir', err);
        this.mostrarMensagem('Erro ao excluir benefício', 'erro');
      }
    });
  }

  salvarBeneficioEvent(b: Beneficio): void {
    if (b.id) {
      this.beneficioService.update(b).subscribe({
        next: () => {
          this.mostrarMensagem('Benefício atualizado com sucesso', 'sucesso');
          this.listar();
          this.beneficioSelecionado = null;
        },
        error: (err) => {
          console.error(err);
          this.mostrarMensagem('Erro ao atualizar benefício', 'erro');
        }
      });
    } else {
      this.beneficioService.create(b).subscribe({
        next: () => {
          this.mostrarMensagem('Benefício criado com sucesso', 'sucesso');
          this.listar();
          this.beneficioSelecionado = null;
        },
        error: (err) => {
          console.error(err);
          this.mostrarMensagem('Erro ao criar benefício', 'erro');
        }
      });
    }
  }

  cancelarEvent(): void {
    this.beneficioSelecionado = null;
  }

  private mostrarMensagem(msg: string, tipo: string): void {
    this.mensagem = msg;
    this.tipoMensagem = tipo;
    setTimeout(() => {
      this.mensagem = '';
      this.tipoMensagem = '';
    }, 3000);
  }
}
