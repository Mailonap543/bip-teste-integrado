import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../shared/services/auth.service';
import { NotificationService } from '../../shared/services/notification.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  template: `
    <div class="login-page">
      <div class="login-card">
        <div class="login-header">
          <div class="login-icon">&#128176;</div>
          <h2>Bem-vindo</h2>
          <p>Acesse o Sistema de Beneficios</p>
        </div>
        <form [formGroup]="form" (ngSubmit)="onSubmit()">
          <div class="form-group">
            <label>Usuario</label>
            <input type="text" formControlName="username" placeholder="Digite seu usuario" />
            <div class="error" *ngIf="form.get('username')?.touched && form.get('username')?.invalid">Obrigatorio</div>
          </div>
          <div class="form-group">
            <label>Senha</label>
            <input [type]="hidePassword ? 'password' : 'text'" formControlName="password" placeholder="Digite sua senha" />
            <div class="error" *ngIf="form.get('password')?.touched && form.get('password')?.invalid">Obrigatorio</div>
          </div>
          <button type="submit" class="btn-primary" [disabled]="form.invalid || loading">
            {{ loading ? 'Entrando...' : 'Entrar' }}
          </button>
        </form>
        <div class="login-footer">
          <p>Nao tem conta? <a (click)="showRegister = !showRegister" class="link">Registre-se</a></p>
        </div>
        <div *ngIf="showRegister" class="register-section">
          <h3>Registro</h3>
          <form [formGroup]="registerForm" (ngSubmit)="onRegister()">
            <div class="form-group">
              <label>Usuario</label>
              <input type="text" formControlName="username" placeholder="Escolha um usuario" />
            </div>
            <div class="form-group">
              <label>Senha</label>
              <input type="password" formControlName="password" placeholder="Escolha uma senha" />
            </div>
            <button type="submit" class="btn-secondary" [disabled]="registerForm.invalid || loading">Criar Conta</button>
          </form>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .login-page { display: flex; justify-content: center; align-items: center; min-height: 80vh; }
    .login-card { background: var(--bg-surface); padding: 40px 32px; border-radius: 12px; box-shadow: var(--shadow-md); width: 100%; max-width: 400px; }
    .login-header { text-align: center; margin-bottom: 28px; }
    .login-icon { font-size: 3rem; }
    .login-header h2 { margin: 8px 0 4px; }
    .login-header p { color: var(--text-secondary); margin: 0; }
    .form-group { margin-bottom: 16px; }
    .form-group label { display: block; font-weight: 600; margin-bottom: 6px; color: var(--text-primary); }
    .form-group input {
      width: 100%; padding: 10px 12px; border: 1px solid var(--border-color);
      border-radius: 6px; font-size: 1rem; box-sizing: border-box; background: var(--bg-surface); color: var(--text-primary);
    }
    .form-group input:focus { outline: none; border-color: var(--primary-500); }
    .error { color: var(--error-500); font-size: 0.85rem; margin-top: 4px; }
    .btn-primary, .btn-secondary {
      width: 100%; padding: 12px; border: none; border-radius: 6px;
      cursor: pointer; font-weight: 600; font-size: 1rem; margin-top: 8px;
    }
    .btn-primary { background: var(--primary-500); color: white; }
    .btn-primary:hover { background: var(--primary-600); }
    .btn-primary:disabled { background: var(--neutral-400); }
    .btn-secondary { background: var(--success-500); color: white; }
    .login-footer { text-align: center; margin-top: 20px; padding-top: 16px; border-top: 1px solid var(--divider); }
    .link { color: var(--primary-500); cursor: pointer; font-weight: 500; }
    .register-section { margin-top: 20px; padding-top: 16px; border-top: 1px solid var(--divider); }
    .register-section h3 { text-align: center; margin-bottom: 12px; }
  `]
})
export class LoginComponent {
  form: FormGroup;
  registerForm: FormGroup;
  loading = false;
  showRegister = false;
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private notification: NotificationService
  ) {
    this.form = this.fb.group({ username: ['', Validators.required], password: ['', Validators.required] });
    this.registerForm = this.fb.group({ username: ['', [Validators.required, Validators.minLength(3)]], password: ['', [Validators.required, Validators.minLength(4)]] });
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.authService.login(this.form.value).subscribe({
      next: (r) => { this.loading = false; if (r.success) { this.notification.success('Login realizado!'); this.router.navigate(['/beneficios']); } else { this.notification.error(r.message); } },
      error: (e) => { this.loading = false; this.notification.error(e.error?.message || 'Erro ao fazer login'); }
    });
  }

  onRegister(): void {
    if (this.registerForm.invalid) return;
    this.loading = true;
    this.authService.register(this.registerForm.value).subscribe({
      next: (r) => { this.loading = false; if (r.success) { this.notification.success('Conta criada!'); this.showRegister = false; this.form.patchValue(this.registerForm.value); } else { this.notification.error(r.message); } },
      error: (e) => { this.loading = false; this.notification.error(e.error?.message || 'Erro ao registrar'); }
    });
  }
}
