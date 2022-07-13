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
  usersEndpoint = "/users";
  paymentsEndpoint = "/payments";
  movementsEndpoint = "/movements";
  httpOptions = {
    headers : new HttpHeaders({
    "Access-Control-Allow-Origin": "*",
    "Content-Type": "application/json",
    })
  };
  users: User[] = [];
  payments: Payment[] = [];
  movements: Movement[] = [];

  paymentForm: FormGroup;
  newUserForm: FormGroup;

  constructor(private http: HttpClient) {
    this.http = http;
    this.readUserList();
    this.readPaymentList();

    this.newUserForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.minLength(1), Validators.maxLength(75)])
    });
    this.paymentForm = new FormGroup({
      description: new FormControl('', [Validators.minLength(3), Validators.required]),
      price: new FormControl('', [Validators.minLength(1), Validators.min(0.01), Validators.required]),
      payerId: new FormControl('', Validators.required)
    });  
  }

  readUserList() {
    this.http
    .get(
      this.apiUrl+this.usersEndpoint, 
      this.httpOptions
    )
    .subscribe({
      next: (response) => {
        this.users = (response as []);
        this.readMovementList();
      },
      error: (error) => console.log(error),
    });
  }

  readMovementList() {
    this.http
    .get(
      this.apiUrl+this.movementsEndpoint, 
      this.httpOptions
    )
    .subscribe({
      next: (response) => {
        this.movements = (response as []);
        this.formatMovements();
      },
      error: (error) => console.log(error),
    });
  }

  readPaymentList() {
    this.http
    .get(
      this.apiUrl+this.paymentsEndpoint, 
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

  createUser() {
    if (!this.newUserForm.valid) {
      this.newUserForm.markAllAsTouched();
      return;
    }

    this.http
      .post(
        this.apiUrl+this.usersEndpoint, 
        JSON.stringify(this.newUserForm.value), 
        this.httpOptions
      )
      .subscribe({
        next: (response) => {
          this.newUserForm.reset();
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
        this.apiUrl+this.paymentsEndpoint, 
        JSON.stringify(this.paymentForm.value), 
        this.httpOptions
      )
      .subscribe({
        next: (response) => {
          this.paymentForm.reset({
            payerId: ''
          });
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

  formatMovements() {
    for (let i = 0; i< this.movements.length; i++) {
      this.movements[i].userOwedMoneyId = this.users.find(user => user.id == this.movements[i].userOwedMoneyId)?.name;
      this.movements[i].userOwingMoneyId = this.users.find(user => user.id == this.movements[i].userOwingMoneyId)?.name;
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

export class Movement {
  userOwedMoneyId!: string | undefined;
  amount!: number;
  userOwingMoneyId!: string | undefined;
}