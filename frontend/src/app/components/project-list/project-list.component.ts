import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RouterModule, Router } from '@angular/router'; 
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
  imports: [FormsModule, CommonModule, RouterModule],
})
export class ProjectListComponent {
  projects: any[] = []; // Speichert alle empfangenen Projekte
  viewId = ''; // Speichert die View-ID für das aktuelle Projekt
  dialogOpen = false; // Steuert, ob der Dialog geöffnet ist

  constructor(private http: HttpClient, private router: Router) {} // Router hinzufügen

  // Öffnet den Eingabedialog
  openDialog(): void {
    this.dialogOpen = true;
  }

  // Schließt den Eingabedialog
  closeDialog(): void {
    this.dialogOpen = false;
  }

  // Ruft ein Projekt vom Backend ab
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
        this.projects.push(parsedData); // Neues Projekt hinzufügen
        this.closeDialog(); // Dialog schließen
        this.viewId = ''; // Zurücksetzen des Eingabefeldes
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

  // Navigiert zur Seite "Gespeicherte Projekte"
  viewSavedProjects(): void {
    console.log("/gespeichterte Projekt anschauen");
    this.router.navigate(['/gespeicherte-projekte']); // Navigiert zur neuen Seite
  }
}
