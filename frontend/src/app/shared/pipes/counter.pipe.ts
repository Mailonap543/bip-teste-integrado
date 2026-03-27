import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'counter',
  standalone: true
})
export class CounterPipe implements PipeTransform {
  transform(value: number, duration: number = 1000): string {
    if (value === null || value === undefined) return '0';
    return value.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
  }
}
