import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FormPersistenceService {

  save(formKey: string, data: any): void {
    try {
      localStorage.setItem(`form_${formKey}`, JSON.stringify(data));
    } catch (e) {
      console.warn('Could not save form to localStorage:', e);
    }
  }

  load<T>(formKey: string): T | null {
    try {
      const data = localStorage.getItem(`form_${formKey}`);
      return data ? JSON.parse(data) : null;
    } catch (e) {
      console.warn('Could not load form from localStorage:', e);
      return null;
    }
  }

  clear(formKey: string): void {
    localStorage.removeItem(`form_${formKey}`);
  }

  clearAll(): void {
    Object.keys(localStorage)
      .filter(key => key.startsWith('form_'))
      .forEach(key => localStorage.removeItem(key));
  }
}
