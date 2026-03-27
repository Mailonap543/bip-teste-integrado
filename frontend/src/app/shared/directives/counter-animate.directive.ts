import { Directive, ElementRef, OnInit, OnDestroy, Input } from '@angular/core';

@Directive({
  selector: '[appCounterAnimate]',
  standalone: true
})
export class CounterAnimateDirective implements OnInit, OnDestroy {
  @Input() appCounterAnimate: number = 0;
  @Input() counterDuration: number = 1000;

  private observer: IntersectionObserver | null = null;
  private animated = false;

  constructor(private el: ElementRef) {}

  ngOnInit(): void {
    this.observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting && !this.animated) {
          this.animate();
          this.animated = true;
        }
      });
    }, { threshold: 0.5 });

    this.observer.observe(this.el.nativeElement);
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }

  private animate(): void {
    const target = this.appCounterAnimate;
    const duration = this.counterDuration;
    const start = performance.now();
    const startValue = 0;

    const step = (timestamp: number) => {
      const progress = Math.min((timestamp - start) / duration, 1);
      const eased = 1 - Math.pow(1 - progress, 3);
      const current = Math.floor(startValue + (target - startValue) * eased);
      this.el.nativeElement.textContent = current.toLocaleString('pt-BR', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

      if (progress < 1) {
        requestAnimationFrame(step);
      }
    };

    requestAnimationFrame(step);
  }
}
