import { TestBed } from '@angular/core/testing';
// ✅ CORREÇÃO: Altera o nome do componente e o caminho de importação.
import { AppComponent } from './app.component'; 
import { RouterTestingModule } from '@angular/router/testing'; 

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // ✅ Componente Standalone no imports
      imports: [AppComponent, RouterTestingModule], 
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  // Você pode adicionar mais testes aqui, se necessário
});