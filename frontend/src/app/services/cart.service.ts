import { Injectable } from '@angular/core';
import { BehaviorSubject, map, tap } from 'rxjs';
import { HttpClient } from '@angular/common/http';

export interface Product {
  id: number;
  name: string;
  category: string;
  price: number;
  unit: string;
  description: string;
}

export interface CartItem {
  productId: number;
  productName: string;
  category: string;
  unit: string;
  unitPrice: number;
  quantity: number;
  totalPrice: number;
}

export interface CartState {
  items: CartItem[];
  total: number;
  status: string;
}

export interface Order {
  orderId: number;
  items: CartItem[];
  total: number;
  placedAt: string;
  status: string;
}

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private cartSubject = new BehaviorSubject<CartState>({ items: [], total: 0, status: 'OPEN' });
  private ordersSubject = new BehaviorSubject<Order[]>([]);

  cart$ = this.cartSubject.asObservable();
  orders$ = this.ordersSubject.asObservable();

  constructor(private http: HttpClient) {
    this.refreshCart();
    this.refreshOrders();
  }

  refreshCart() {
    this.http.get<CartState>('/api/cart').subscribe((cart) => this.cartSubject.next(cart));
  }

  refreshOrders() {
    this.http.get<Order[]>('/api/orders').subscribe((orders) => this.ordersSubject.next(orders));
  }

  addToCart(productId: number, quantity = 1) {
    return this.http.post<CartState>('/api/cart/items', { productId, quantity }).pipe(
      tap((cart) => this.cartSubject.next(cart))
    );
  }

  updateQuantity(productId: number, quantity: number) {
    return this.http.put<CartState>(`/api/cart/items/${productId}`, { quantity }).pipe(
      tap((cart) => this.cartSubject.next(cart))
    );
  }

  removeFromCart(productId: number) {
    return this.http.delete<CartState>(`/api/cart/items/${productId}`).pipe(
      tap((cart) => this.cartSubject.next(cart))
    );
  }

  placeOrder() {
    return this.http.post<Order>('/api/cart/checkout', {}).pipe(
      tap(() => {
        this.refreshCart();
        this.refreshOrders();
      })
    );
  }
}
