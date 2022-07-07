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

  constructor(private http: HttpClient) {
    this.http = http;
  }

  createUser(form: NgForm) {
    this.http
      .post(
        this.apiUrl+'/users', 
        JSON.stringify(form.value), 
        {
          headers : new HttpHeaders({
          "Access-Control-Allow-Origin": "*",
          "Content-Type": "application/json",
        })
      })
      .subscribe({
        next: (response) => console.log(response),
        error: (error) => console.log(error),
      });
  }

}