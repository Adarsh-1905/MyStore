import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { CartService, Product } from '../../services/cart.service';

@Component({
  selector: 'app-products',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css'],
})
export class ProductsComponent implements OnInit {
  products: Product[] = [];
  loading = true;
  errorMessage = '';
  successMessage = '';

  constructor(private http: HttpClient, private cartService: CartService) {}

  ngOnInit(): void {
    this.http.get<Product[]>('/api/products').subscribe({
      next: (data) => {
        this.products = data;
        this.loading = false;
      },
      error: (error) => {
        console.error('Failed to load products', error);
        this.errorMessage = 'Unable to load products right now. Please try again later.';
        this.loading = false;
      },
    });
  }

  addToCart(product: Product): void {
    this.cartService.addToCart(product.id).subscribe(() => {
      this.successMessage = `${product.name} added to cart.`;
      window.setTimeout(() => this.successMessage = '', 2200);
    });
  }
}
