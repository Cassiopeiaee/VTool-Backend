import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  getProjects(viewId: string, filter: string, format: string): Observable<string> {
    const url = `${this.apiUrl}?viewId=${viewId}&filter=${filter}&format=${format}`;
    return this.http.get(url, { responseType: 'text' });
  }
}
