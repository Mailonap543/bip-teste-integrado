import { Directive, Input, TemplateRef, ViewContainerRef, OnInit, OnDestroy } from '@angular/core';

@Directive({
  selector: '[appVirtualScroll]',
  standalone: true
})
export class VirtualScrollDirective<T> implements OnInit, OnDestroy {
  @Input() appVirtualScrollOf: T[] = [];
  @Input() appVirtualScrollItemSize: number = 60;
  @Input() appVirtualScrollContainerHeight: number = 400;

  private scrollHandler: (() => void) | null = null;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef
  ) {}

  ngOnInit(): void {
    this.render();
  }

  ngOnDestroy(): void {
    if (this.scrollHandler) {
      window.removeEventListener('scroll', this.scrollHandler);
    }
  }

  private render(): void {
    this.viewContainer.clear();
    const visibleCount = Math.ceil(this.appVirtualScrollContainerHeight / this.appVirtualScrollItemSize);
    const items = this.appVirtualScrollOf.slice(0, Math.min(visibleCount, this.appVirtualScrollOf.length));

    items.forEach((item, index) => {
      this.viewContainer.createEmbeddedView(this.templateRef, {
        $implicit: item,
        index: index
      });
    });
  }
}
