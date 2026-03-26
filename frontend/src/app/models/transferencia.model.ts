export interface Transferencia {
  fromId: number;
  toId: number;
  amount: number;
}

export interface TransferenciaResponse {
  origemId: number;
  destinoId: number;
  valor: number;
  data?: string;
}
