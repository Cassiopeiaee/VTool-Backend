import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-saved-projects',
  templateUrl: './saved-projects.component.html',
  styleUrls: ['./saved-projects.component.css'],
  imports: [CommonModule],
})
export class SavedProjectsComponent implements OnInit {
  savedProjects: any[] = []; 

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchSavedProjects();
  }

  // API-Aufruf zum Abrufen der gespeicherten Projekte
  fetchSavedProjects(): void {
    const apiUrl = 'http://localhost:8080/smenso/api/saved-projects'; // Aktualisiere die URL entsprechend deinem Backend
    this.http.get<any[]>(apiUrl).subscribe({
      next: (data) => {
        this.savedProjects = data;
      },
      error: (error) => {
        console.error('Fehler beim Abrufen der gespeicherten Projekte:', error);
        alert('Fehler beim Laden der gespeicherten Projekte.');
      },
    });
  }
}
