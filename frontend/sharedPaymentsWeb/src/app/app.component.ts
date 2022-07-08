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

  constructor(private http: HttpClient) {
    this.http = http;
    this.readUserList();
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
        next: (response) => this.readUserList(),
        error: (error) => console.log(error),
      });
  }

}

export class User {
  debt!: number;
  name!: string;
  id!: string;
}