import { Injectable, signal } from '@angular/core';

export interface Notification {
  id: number;
  message: string;
  type: 'success' | 'error' | 'info' | 'warning';
  undoAction?: () => void;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private counter = 0;
  notifications = signal<Notification[]>([]);

  success(message: string, undoAction?: () => void): void { this.show(message, 'success', undoAction); }
  error(message: string): void { this.show(message, 'error'); }
  info(message: string): void { this.show(message, 'info'); }
  warning(message: string): void { this.show(message, 'warning'); }

  private show(message: string, type: 'success' | 'error' | 'info' | 'warning', undoAction?: () => void): void {
    const id = ++this.counter;
    const notification: Notification = { id, message, type, undoAction };
    this.notifications.update(list => [...list, notification]);

    setTimeout(() => this.dismiss(id), undoAction ? 5000 : 3000);
  }

  undo(id: number): void {
    const notification = this.notifications().find(n => n.id === id);
    if (notification?.undoAction) {
      notification.undoAction();
    }
    this.dismiss(id);
  }

  dismiss(id: number): void {
    this.notifications.update(list => list.filter(n => n.id !== id));
  }
}
