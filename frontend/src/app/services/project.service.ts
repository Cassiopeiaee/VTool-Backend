import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  private apiUrl = 'http://localhost:8080/projects/report';

  constructor(private http: HttpClient) {}

  getProjects(viewId: string, filter: string, format: string): Observable<string> {
    return this.http.get(`${this.apiUrl}?viewId=${viewId}&filter=${filter}&format=${format}`, {
      responseType: 'text',
    });
  }
}
