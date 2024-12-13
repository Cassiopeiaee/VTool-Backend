// src/app/saved-project/saved-project.component.ts

import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; 
import { FormsModule } from '@angular/forms'; 

@Component({
  standalone: true,
  selector: 'app-saved-projects',
  templateUrl: './saved-projects.component.html',
  styleUrls: ['./saved-projects.component.css'],
  imports: [CommonModule, FormsModule, HttpClientModule], // HttpClientModule hinzugefügt
})
export class SavedProjectsComponent implements OnInit {
  savedProjects: any[] = []; 

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchSavedProjects();
  }

  fetchSavedProjects(): void {
    const apiUrl = 'http://localhost:8080/smenso/api/saved-projects'; 
    this.http.get<any[]>(apiUrl).subscribe({
      next: (data) => {
        this.savedProjects = data;
        console.log('Gespeicherte Projekte:', this.savedProjects); // Überprüfen Sie die Daten
      },
      error: (error) => {
        console.error('Fehler beim Abrufen der gespeicherten Projekte:', error);
        alert('Fehler beim Laden der gespeicherten Projekte.');
      },
    });
  }

  goBackToMain(): void {
    this.router.navigate(['/']); 
  }

  downloadProjectAsExcel(id: string): void {
    const downloadUrl = `http://localhost:8080/smenso/download-project/${id}`;
    this.http.get(downloadUrl, { responseType: 'blob' }).subscribe({
      next: (data: Blob) => {
        const blobUrl = URL.createObjectURL(data);
        const a = document.createElement('a');
        a.href = blobUrl;
        a.download = `project_${id}.xlsx`;
        a.click();
        URL.revokeObjectURL(blobUrl);
      },
      error: (error) => {
        console.error('Fehler beim Herunterladen der Excel-Datei:', error);
        alert('Fehler beim Herunterladen der Datei.');
      }
    });
  }
}
