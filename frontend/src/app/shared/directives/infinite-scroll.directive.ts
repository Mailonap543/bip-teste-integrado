import { Directive, ElementRef, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';

@Directive({
  selector: '[appInfiniteScroll]',
  standalone: true
})
export class InfiniteScrollDirective implements OnInit, OnDestroy {
  @Output() scrolled = new EventEmitter<void>();

  private observer: IntersectionObserver | null = null;

  constructor(private el: ElementRef) {}

  ngOnInit(): void {
    this.observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting) {
          this.scrolled.emit();
        }
      },
      { threshold: 0.1 }
    );
    this.observer.observe(this.el.nativeElement);
  }

  ngOnDestroy(): void {
    this.observer?.disconnect();
  }
}
