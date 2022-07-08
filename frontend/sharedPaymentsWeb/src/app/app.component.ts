import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = "Shared Payments WebApp";
  apiUrl = "http://localhost:8080/api/v1";
  httpOptions = {
    headers : new HttpHeaders({
    "Access-Control-Allow-Origin": "*",
    "Content-Type": "application/json",
    })
  };
  users: User[] = [];
  payments: Payment[] = [];

  constructor(private http: HttpClient) {
    this.http = http;
    this.readUserList();
    this.readPaymentList();
  }

  readUserList() {
    this.http
    .get(
      this.apiUrl+'/users', 
      this.httpOptions
    )
    .subscribe({
      next: (response) => {
        this.users = (response as []);
      },
      error: (error) => console.log(error),
    });
  }

  readPaymentList() {
    this.http
    .get(
      this.apiUrl+'/payments', 
      this.httpOptions
    )
    .subscribe({
      next: (response) => {
        this.payments = (response as []);
        this.formatPayments();
      },
      error: (error) => console.log(error),
    });
  }

  createUser(form: NgForm) {
    this.http
      .post(
        this.apiUrl+'/users', 
        JSON.stringify(form.value), 
        this.httpOptions
      )
      .subscribe({
        next: (response) => this.users.push(response as User),
        error: (error) => console.log(error),
      });
  }

  createPayment(form: NgForm) {
    console.log(form.value);
    this.http
      .post(
        this.apiUrl+'/payments', 
        JSON.stringify(form.value), 
        this.httpOptions
      )
      .subscribe({
        next: (response) => {
          this.readUserList();
          this.readPaymentList();
        },
        error: (error) => console.log(error),
      });
  }

  formatPayments() {
    for (let i = 0; i < this.payments.length; i++) {
      this.payments[i].paymentDate = new Date(this.payments[i].paymentDate).toLocaleString();
      this.payments[i].payerName = this.users.find(user => user.id == this.payments[i].payerId)?.name;
    }
  }
}

export class User {
  debt!: number;
  name!: string;
  id!: string;
}

export class Payment {
  price!: number;
  paymentDate!: string;
  payerId!: string;
  description!: string;
  id!: string;
  payerName!: string | undefined;
}