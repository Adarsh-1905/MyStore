import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CartService, CartItem } from '../../services/cart.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css'],
})
export class CartComponent implements OnInit {
  cartItems: CartItem[] = [];
  orderPlaced = false;
  successMessage = '';
  total = 0;

  constructor(private cartService: CartService) {}

  ngOnInit(): void {
    this.cartService.cart$.subscribe((cart) => {
      this.cartItems = cart.items;
      this.total = cart.total;
      if (!cart.items.length) {
        this.successMessage = '';
      }
    });
  }

  removeItem(productId: number): void {
    this.cartService.removeFromCart(productId).subscribe();
  }

  updateQuantity(item: CartItem, event: Event): void {
    const value = Number((event.target as HTMLInputElement).value || 1);
    this.cartService.updateQuantity(item.productId, value).subscribe();
  }

  placeOrder(): void {
    if (!this.cartItems.length) {
      return;
    }
    this.cartService.placeOrder().subscribe((order) => {
      this.orderPlaced = true;
      this.successMessage = `Order placed successfully! Order #${order.orderId}`;
    });
  }
}
