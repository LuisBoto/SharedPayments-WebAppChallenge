import { Component } from '@angular/core';
import { environment } from './../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormControl, FormGroup, NgForm, Validators } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  title = "Shared Payments WebApp";
  apiUrl = environment.apiUrl;
  httpOptions = {
    headers : new HttpHeaders({
    "Access-Control-Allow-Origin": "*",
    "Content-Type": "application/json",
    })
  };
  users: User[] = [];
  payments: Payment[] = [];

  paymentForm: FormGroup;

  constructor(private http: HttpClient) {
    this.http = http;
    this.readUserList();
    this.readPaymentList();

    this.paymentForm = new FormGroup({
      description: new FormControl('', [Validators.minLength(3), Validators.required]),
      price: new FormControl('', [Validators.minLength(1), Validators.min(0.01), Validators.required]),
      payerId: new FormControl('', Validators.required)
    });  
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
        next: (response) => {
          form.reset();
          this.users.push(response as User);
        },
        error: (error) => console.log(error),
      });
  }

  createPayment() {
    if (!this.paymentForm.valid) {
      this.paymentForm.markAllAsTouched();
      return;
    }
    console.log(this.paymentForm.value);
    this.http
      .post(
        this.apiUrl+'/payments', 
        JSON.stringify(this.paymentForm.value), 
        this.httpOptions
      )
      .subscribe({
        next: (response) => {
          this.paymentForm.reset();
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