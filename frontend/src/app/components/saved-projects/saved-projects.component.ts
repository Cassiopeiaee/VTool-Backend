// src/app/saved-projects/saved-projects.component.ts

import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; 
import { FormsModule } from '@angular/forms'; 

@Component({
  standalone: true,
  selector: 'app-saved-projects',
  templateUrl: './saved-projects.component.html',
  styleUrls: ['./saved-projects.component.css'],
  imports: [CommonModule, FormsModule],
})
export class SavedProjectsComponent implements OnInit {
  savedProjects: any[] = []; 
  filteredProjects: any[] = [];
  searchTerm: string = '';
  filterTerm: string = '';
  projectNames: string[] = []; // Liste der einzigartigen Projektnamen für das Filter-Dropdown

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.fetchSavedProjects();
  }

  /**
   * Lädt die gespeicherten Projekte von der API.
   */
  fetchSavedProjects(): void {
    const apiUrl = 'http://localhost:8080/smenso/api/saved-projects'; 
    this.http.get<any[]>(apiUrl).subscribe({
      next: (data) => {
        this.savedProjects = data;
        console.log('Gespeicherte Projekte:', this.savedProjects);
        // Extrahiere einzigartige Projektnamen für das Filter-Dropdown
        this.projectNames = Array.from(new Set(this.savedProjects.map(p => p.title))).sort();
        this.filterProjects(); // Initiale Filterung nach dem Laden der Daten
      },
      error: (error) => {
        console.error('Fehler beim Abrufen der gespeicherten Projekte:', error);
        alert('Fehler beim Laden der gespeicherten Projekte.');
      },
    });
  }

  /**
   * Filtert die Projekte basierend auf dem Suchbegriff und dem ausgewählten Filter.
   */
  filterProjects(): void {
    this.filteredProjects = this.savedProjects.filter(project => {
      const matchesSearch = project.title.toLowerCase().includes(this.searchTerm.toLowerCase());
      const matchesFilter = this.filterTerm === '' || project.title === this.filterTerm;
      return matchesSearch && matchesFilter;
    });
  }

  /**
   * Navigiert zurück zur Hauptseite.
   */
  goBackToMain(): void {
    this.router.navigate(['/']); 
  }

  /**
   * Navigiert zu einem bestimmten Projekt in der Tabelle.
   * @param projectId - Die ID des Projekts.
   */
  navigateToProject(projectId: string): void {
    const element = document.getElementById(projectId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  /**
   * Lädt ein Projekt als Excel-Datei herunter.
   * @param id - Die ID des Projekts.
   */
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
