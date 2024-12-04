import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
  imports: [FormsModule, CommonModule],
})
export class ProjectListComponent {
  projects: any[] = []; 
  viewId = ''; 
  dialogOpen = false; 

  constructor(private http: HttpClient) {}

  
  openDialog(): void {
    this.dialogOpen = true;
  }

  
  closeDialog(): void {
    this.dialogOpen = false;
  }

  // Ruft die Daten vom Backend ab
  fetchProject(): void {
    if (!this.viewId.trim()) {
      alert('Bitte geben Sie eine gültige View-ID ein.');
      return;
    }

    const backendUrl = `http://localhost:8080/smenso/report/${this.viewId}`;
    console.log('API-URL:', backendUrl);

    this.http.get<string>(backendUrl, { responseType: 'text' as 'json' }).subscribe({
      next: (response) => {
        console.log('CSV-Antwort:', response);
        const parsedData = this.parseCsvData(response); // CSV-Daten parsen
        this.projects = [parsedData]; // Aktualisiere die Tabelle
        this.closeDialog(); 
      },
      error: (error) => {
        console.error('Fehler beim Abrufen des Projekts:', error);
        alert('Fehler beim Abrufen des Projekts. Bitte prüfen Sie die View-ID.');
      },
    });
  }

  // Parsed CSV-Daten in ein Array von Objekten
  private parseCsvData(csv: string): any {
    const lines = csv.split('\n');
    const headers = lines[0]?.split(',');
    const values = lines[1]?.split(',');

    if (!headers || !values) {
      console.error('Ungültige CSV-Daten:', csv);
      return {};
    }

    const project: any = {};
    headers.forEach((header, index) => {
      project[header.trim()] = values[index]?.trim() || 'Nicht verfügbar';
    });

    return project;
  }
}
