import { Component, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../shared/services/auth.service';
import { NotificationService } from '../../shared/services/notification.service';
import { PasswordStrengthComponent } from '../../shared/components/password-strength/password-strength.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, PasswordStrengthComponent],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="auth-page">
      <div class="auth-bg">
        <div class="bg-circle circle-1"></div>
        <div class="bg-circle circle-2"></div>
        <div class="bg-circle circle-3"></div>
      </div>

      <div class="auth-card" [class.register-mode]="!isLogin">
        <div class="card-glow"></div>

        <div class="card-header">
          <div class="logo-ring">
            <span class="logo-icon">$</span>
          </div>
          <h2>{{ isLogin ? 'Bem-vindo de volta' : 'Criar nova conta' }}</h2>
          <p>{{ isLogin ? 'Entre com suas credenciais' : 'Preencha os dados para comecar' }}</p>
        </div>

        <!-- LOGIN FORM -->
        <form *ngIf="isLogin" [formGroup]="form" (ngSubmit)="onSubmit()" class="auth-form">
          <div class="input-wrapper" [class.error]="form.get('username')?.touched && form.get('username')?.invalid">
            <span class="input-icon">U</span>
            <input type="text" formControlName="username" placeholder="Usuario" autocomplete="username" />
            <div class="input-line"></div>
          </div>
          <div class="field-error" *ngIf="form.get('username')?.touched && form.get('username')?.hasError('required')">
            Usuario e obrigatorio
          </div>

          <div class="input-wrapper" [class.error]="form.get('password')?.touched && form.get('password')?.invalid">
            <span class="input-icon">*</span>
            <input [type]="hidePass ? 'password' : 'text'" formControlName="password" placeholder="Senha" autocomplete="current-password" />
            <button type="button" class="toggle-pass" (click)="hidePass = !hidePass">
              {{ hidePass ? 'O' : '-' }}
            </button>
            <div class="input-line"></div>
          </div>
          <div class="field-error" *ngIf="form.get('password')?.touched && form.get('password')?.hasError('required')">
            Senha e obrigatoria
          </div>

          <div class="form-options">
            <label class="check-label">
              <input type="checkbox" formControlName="remember" />
              <span class="checkmark"></span>
              Manter conectado
            </label>
          </div>

          <button type="submit" class="btn-primary" [disabled]="form.invalid || loading">
            <span *ngIf="loading" class="spinner"></span>
            {{ loading ? 'Entrando...' : 'Entrar' }}
          </button>
        </form>

        <!-- REGISTER FORM -->
        <form *ngIf="!isLogin" [formGroup]="registerForm" (ngSubmit)="onRegister()" class="auth-form">
          <div class="input-wrapper">
            <span class="input-icon">U</span>
            <input type="text" formControlName="username" placeholder="Escolha um usuario" autocomplete="username" />
            <div class="input-line"></div>
          </div>

          <div class="input-wrapper">
            <span class="input-icon">*</span>
            <input [type]="hidePass ? 'password' : 'text'" formControlName="password" placeholder="Escolha uma senha" autocomplete="new-password" />
            <button type="button" class="toggle-pass" (click)="hidePass = !hidePass">
              {{ hidePass ? 'O' : '-' }}
            </button>
            <div class="input-line"></div>
          </div>
          <app-password-strength [password]="registerForm.get('password')?.value || ''"></app-password-strength>

          <button type="submit" class="btn-register" [disabled]="registerForm.invalid || loading">
            <span *ngIf="loading" class="spinner"></span>
            {{ loading ? 'Criando...' : 'Criar Conta' }}
          </button>
        </form>

        <div class="card-footer">
          <p *ngIf="isLogin">
            Nao tem conta? <a (click)="toggleAuth()" class="link">Criar conta</a>
          </p>
          <p *ngIf="!isLogin">
            Ja tem conta? <a (click)="toggleAuth()" class="link">Entrar</a>
          </p>
        </div>
      </div>
    </div>
  `,
  styles: [`
    .auth-page {
      min-height: 100vh; display: flex; align-items: center; justify-content: center;
      background: #0f172a; position: relative; overflow: hidden; padding: 20px;
    }
    .auth-bg { position: absolute; inset: 0; overflow: hidden; }
    .bg-circle {
      position: absolute; border-radius: 50%; filter: blur(80px); opacity: 0.4;
      animation: float 8s ease-in-out infinite;
    }
    .circle-1 { width: 400px; height: 400px; background: #3b82f6; top: -100px; left: -100px; animation-delay: 0s; }
    .circle-2 { width: 300px; height: 300px; background: #8b5cf6; bottom: -50px; right: -50px; animation-delay: 2s; }
    .circle-3 { width: 200px; height: 200px; background: #06b6d4; top: 50%; left: 50%; animation-delay: 4s; }
    @keyframes float {
      0%, 100% { transform: translate(0, 0) scale(1); }
      33% { transform: translate(30px, -30px) scale(1.05); }
      66% { transform: translate(-20px, 20px) scale(0.95); }
    }

    .auth-card {
      position: relative; width: 100%; max-width: 420px; padding: 40px;
      background: rgba(255, 255, 255, 0.04);
      backdrop-filter: blur(20px);
      -webkit-backdrop-filter: blur(20px);
      border: 1px solid rgba(255, 255, 255, 0.08);
      border-radius: 24px;
      box-shadow: 0 25px 60px rgba(0, 0, 0, 0.5);
      animation: cardIn 0.5s ease-out;
      overflow: hidden;
      transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
    }
    .auth-card:hover {
      border-color: rgba(255, 255, 255, 0.12);
      box-shadow: 0 30px 70px rgba(0, 0, 0, 0.6), 0 0 40px rgba(59, 130, 246, 0.1);
    }
    @keyframes cardIn {
      from { opacity: 0; transform: translateY(30px) scale(0.95); }
      to { opacity: 1; transform: translateY(0) scale(1); }
    }

    .card-glow {
      position: absolute; top: -2px; left: 50%; transform: translateX(-50%);
      width: 60%; height: 2px;
      background: linear-gradient(90deg, transparent, rgba(59, 130, 246, 0.6), transparent);
      border-radius: 2px;
    }

    .card-header { text-align: center; margin-bottom: 32px; }
    .logo-ring {
      width: 60px; height: 60px; margin: 0 auto 16px;
      background: linear-gradient(135deg, rgba(59, 130, 246, 0.2), rgba(139, 92, 246, 0.2));
      border: 2px solid rgba(59, 130, 246, 0.3);
      border-radius: 50%; display: flex; align-items: center; justify-content: center;
      animation: pulseRing 2s ease-in-out infinite;
    }
    @keyframes pulseRing {
      0%, 100% { box-shadow: 0 0 0 0 rgba(59, 130, 246, 0.2); }
      50% { box-shadow: 0 0 0 12px rgba(59, 130, 246, 0); }
    }
    .logo-icon { font-size: 1.8rem; font-weight: 800; color: #60a5fa; }
    .card-header h2 { color: #f1f5f9; font-size: 1.5rem; font-weight: 700; margin-bottom: 6px; }
    .card-header p { color: #94a3b8; font-size: 0.9rem; margin: 0; }

    .auth-form { animation: formSlide 0.3s ease-out; }
    @keyframes formSlide { from { opacity: 0; transform: translateX(20px); } to { opacity: 1; transform: translateX(0); } }

    .input-wrapper {
      position: relative; display: flex; align-items: center; gap: 12px;
      border-bottom: 2px solid rgba(255, 255, 255, 0.1);
      padding: 8px 0; margin-bottom: 8px; transition: border-color 0.3s;
    }
    .input-wrapper:focus-within { border-color: #3b82f6; }
    .input-wrapper.error { border-color: #ef4444; }
    .input-icon { color: #64748b; font-size: 0.9rem; font-weight: 700; min-width: 16px; text-align: center; }
    .input-wrapper input {
      flex: 1; background: none; border: none; outline: none;
      color: #f1f5f9; font-size: 1rem; padding: 8px 0;
    }
    .input-wrapper input::placeholder { color: #475569; }
    .input-line {
      position: absolute; bottom: -2px; left: 0; width: 0; height: 2px;
      background: linear-gradient(90deg, #3b82f6, #8b5cf6);
      transition: width 0.3s ease;
    }
    .input-wrapper:focus-within .input-line { width: 100%; }
    .toggle-pass {
      background: none; border: none; cursor: pointer;
      color: #64748b; font-size: 0.9rem; padding: 4px; transition: color 0.2s;
    }
    .toggle-pass:hover { color: #94a3b8; }
    .field-error { color: #f87171; font-size: 0.75rem; margin-bottom: 8px; }

    .form-options { margin: 16px 0 24px; }
    .check-label {
      display: flex; align-items: center; gap: 8px;
      color: #94a3b8; font-size: 0.85rem; cursor: pointer;
    }
    .check-label input { display: none; }
    .checkmark {
      width: 18px; height: 18px; border: 2px solid rgba(255,255,255,0.15);
      border-radius: 4px; transition: all 0.2s; position: relative;
    }
    .check-label input:checked + .checkmark {
      background: #3b82f6; border-color: #3b82f6;
    }
    .check-label input:checked + .checkmark::after {
      content: ''; position: absolute; top: 2px; left: 5px;
      width: 4px; height: 8px; border: solid white; border-width: 0 2px 2px 0;
      transform: rotate(45deg);
    }

    .btn-primary, .btn-register {
      width: 100%; padding: 14px; border: none; border-radius: 40px;
      font-size: 1rem; font-weight: 700; cursor: pointer;
      transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
      display: flex; align-items: center; justify-content: center; gap: 8px;
    }
    .btn-primary {
      background: linear-gradient(90deg, #00d2ff, #3a7bd5);
      color: white;
      box-shadow: 0 4px 20px rgba(58, 123, 213, 0.3);
    }
    .btn-primary:hover:not(:disabled) {
      transform: scale(1.03);
      box-shadow: 0 6px 30px rgba(58, 123, 213, 0.5);
    }
    .btn-register {
      background: linear-gradient(90deg, #10b981, #34d399);
      color: white;
      box-shadow: 0 4px 20px rgba(16, 185, 129, 0.3);
    }
    .btn-register:hover:not(:disabled) {
      transform: scale(1.03);
      box-shadow: 0 6px 30px rgba(16, 185, 129, 0.5);
    }
    .btn-primary:disabled, .btn-register:disabled { opacity: 0.5; cursor: not-allowed; transform: none !important; }

    .spinner {
      width: 18px; height: 18px; border: 2px solid rgba(255,255,255,0.3);
      border-top-color: white; border-radius: 50%; animation: spin 0.7s linear infinite;
    }
    @keyframes spin { to { transform: rotate(360deg); } }

    .card-footer { text-align: center; margin-top: 24px; }
    .card-footer p { color: #64748b; font-size: 0.9rem; margin: 0; }
    .link { color: #60a5fa; cursor: pointer; font-weight: 600; transition: color 0.2s; }
    .link:hover { color: #93c5fd; }
  `]
})
export class LoginComponent {
  form: FormGroup;
  registerForm: FormGroup;
  loading = false;
  hidePass = true;
  isLogin = true;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private notification: NotificationService,
    private cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      remember: [false]
    });
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(4)]]
    });
  }

  toggleAuth(): void {
    this.isLogin = !this.isLogin;
    this.cdr.markForCheck();
  }

  onSubmit(): void {
    if (this.form.invalid) return;
    this.loading = true;
    this.authService.login(this.form.value).subscribe({
      next: (r) => {
        this.loading = false;
        if (r.success) { this.notification.success('Login realizado!'); this.router.navigate(['/']); }
        else { this.notification.error(r.message); }
        this.cdr.markForCheck();
      },
      error: (e) => { this.loading = false; this.notification.error(e.error?.message || 'Erro ao fazer login'); this.cdr.markForCheck(); }
    });
  }

  onRegister(): void {
    if (this.registerForm.invalid) return;
    this.loading = true;
    this.authService.register(this.registerForm.value).subscribe({
      next: (r) => {
        this.loading = false;
        if (r.success) {
          this.notification.success('Conta criada! Faca login.');
          this.isLogin = true;
          this.form.patchValue(this.registerForm.value);
        }
        else { this.notification.error(r.message); }
        this.cdr.markForCheck();
      },
      error: (e) => { this.loading = false; this.notification.error(e.error?.message || 'Erro ao registrar'); this.cdr.markForCheck(); }
    });
  }
}
