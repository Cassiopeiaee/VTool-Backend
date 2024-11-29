import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Project {
    id: string;
    title: string;
    description: string;
    start: string;
    end: string;
}

@Injectable({
    providedIn: 'root',
})
export class ProjectService {
    private apiUrl = 'http://localhost:8080/api/projects'; // Backend-URL

    constructor(private http: HttpClient) {}

    getProjects(): Observable<Project[]> {
        return this.http.get<Project[]>(this.apiUrl);
    }
}
