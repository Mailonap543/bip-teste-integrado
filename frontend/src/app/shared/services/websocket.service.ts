import { Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { BehaviorSubject, Observable } from 'rxjs';

export interface WebSocketMessage {
  type: string;
  data: any;
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private client: Client | null = null;
  private messages$ = new BehaviorSubject<WebSocketMessage | null>(null);
  private connected$ = new BehaviorSubject<boolean>(false);

  connect(): void {
    if (this.client?.connected) return;

    this.client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      onConnect: () => {
        this.connected$.next(true);
        this.subscribeToTopics();
      },
      onDisconnect: () => {
        this.connected$.next(false);
      },
      onStompError: (frame) => {
        console.error('STOMP error:', frame.headers['message']);
      }
    });

    this.client.activate();
  }

  private subscribeToTopics(): void {
    this.client?.subscribe('/topic/beneficios', (message: Message) => {
      const parsed = JSON.parse(message.body);
      this.messages$.next({ type: 'BENEFICIO_UPDATE', data: parsed, timestamp: new Date().toISOString() });
    });

    this.client?.subscribe('/topic/transferencias', (message: Message) => {
      const parsed = JSON.parse(message.body);
      this.messages$.next({ type: 'TRANSFERENCIA_UPDATE', data: parsed, timestamp: new Date().toISOString() });
    });
  }

  disconnect(): void {
    this.client?.deactivate();
    this.connected$.next(false);
  }

  getMessages(): Observable<WebSocketMessage | null> {
    return this.messages$.asObservable();
  }

  isConnected(): Observable<boolean> {
    return this.connected$.asObservable();
  }

  send(destination: string, body: any): void {
    if (this.client?.connected) {
      this.client.publish({ destination, body: JSON.stringify(body) });
    }
  }
}
