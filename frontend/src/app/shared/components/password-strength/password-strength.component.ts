import { Component, Input, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-password-strength',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="strength-container" *ngIf="password">
      <div class="strength-bar">
        <div class="strength-fill" [style.width.%]="strength" [class]="strengthClass"></div>
      </div>
      <div class="strength-label">{{ strengthLabel }}</div>
      <div class="strength-rules">
        <div class="rule" [class.valid]="hasMinLength"><span class="icon">{{ hasMinLength ? '&#10003;' : '&#10007;' }}</span> 8+ caracteres</div>
        <div class="rule" [class.valid]="hasUppercase"><span class="icon">{{ hasUppercase ? '&#10003;' : '&#10007;' }}</span> Maiuscula</div>
        <div class="rule" [class.valid]="hasLowercase"><span class="icon">{{ hasLowercase ? '&#10003;' : '&#10007;' }}</span> Minuscula</div>
        <div class="rule" [class.valid]="hasNumber"><span class="icon">{{ hasNumber ? '&#10003;' : '&#10007;' }}</span> Numero</div>
        <div class="rule" [class.valid]="hasSpecial"><span class="icon">{{ hasSpecial ? '&#10003;' : '&#10007;' }}</span> Simbolo</div>
      </div>
    </div>
  `,
  styles: [`
    .strength-container { margin-top: 6px; }
    .strength-bar { height: 4px; background: var(--border-color); border-radius: 2px; overflow: hidden; }
    .strength-fill { height: 100%; border-radius: 2px; transition: width 0.3s ease, background 0.3s ease; }
    .strength-fill.weak { background: #ef4444; }
    .strength-fill.fair { background: #f59e0b; }
    .strength-fill.good { background: #3b82f6; }
    .strength-fill.strong { background: #10b981; }
    .strength-label { font-size: 0.75rem; margin-top: 4px; color: var(--text-secondary); }
    .strength-rules { display: grid; grid-template-columns: 1fr 1fr; gap: 2px 12px; margin-top: 6px; }
    .rule { font-size: 0.7rem; color: var(--text-muted); transition: color 0.2s; }
    .rule.valid { color: #10b981; }
    .icon { margin-right: 4px; }
  `]
})
export class PasswordStrengthComponent {
  @Input() password: string = '';

  get hasMinLength(): boolean { return this.password.length >= 8; }
  get hasUppercase(): boolean { return /[A-Z]/.test(this.password); }
  get hasLowercase(): boolean { return /[a-z]/.test(this.password); }
  get hasNumber(): boolean { return /[0-9]/.test(this.password); }
  get hasSpecial(): boolean { return /[!@#$%^&*(),.?":{}|<>]/.test(this.password); }

  get strength(): number {
    let score = 0;
    if (this.hasMinLength) score += 20;
    if (this.hasUppercase) score += 20;
    if (this.hasLowercase) score += 20;
    if (this.hasNumber) score += 20;
    if (this.hasSpecial) score += 20;
    return score;
  }

  get strengthClass(): string {
    if (this.strength <= 20) return 'weak';
    if (this.strength <= 40) return 'fair';
    if (this.strength <= 60) return 'good';
    return 'strong';
  }

  get strengthLabel(): string {
    if (this.strength <= 20) return 'Fraca';
    if (this.strength <= 40) return 'Regular';
    if (this.strength <= 60) return 'Boa';
    return 'Forte';
  }
}
