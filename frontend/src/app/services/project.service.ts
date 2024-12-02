import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ProjectService {
  private apiUrl = 'http://localhost:8080/api/projects/report/json';

  constructor(private http: HttpClient) {}

  getProjects(viewId: string, filter: string, format: string = 'CSV'): Observable<any[]> {
    const url = `${this.apiUrl}?viewId=${viewId}&filter=${filter}&format=${format}`;
    return this.http.get<any[]>(url);
  }
}
