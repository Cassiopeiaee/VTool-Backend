import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; 
import { CommonModule } from '@angular/common'; 
import { HttpClient } from '@angular/common/http'; 

@Component({
  standalone: true,
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
  imports: [FormsModule, CommonModule], // FormsModule hier hinzufügen
})
export class ProjectListComponent {
  projects: any[] = []; // Array zum Speichern der Projekt-Daten
  dialogOpen = false; 
  viewId = ''; 

  constructor(private http: HttpClient) {} // Inject HttpClient für API-Requests

  openDialog(): void {
    this.dialogOpen = true; 
  }

  closeDialog(): void {
    this.dialogOpen = false; 
  }

  fetchProject(): void {
    if (!this.viewId.trim()) {
      alert('Bitte geben Sie eine gültige View-ID ein.'); // Validierung der Eingabe
      return;
    }
  
    const apiUrl = `https://bgn-it.smenso.cloud/skyisland/api/Reports/projects/${this.viewId}?view=e813c779-f5ed-4fce-91ca-1ec9f67b0262&filter=active&format=CSV`;
  
    console.log('API-URL:', apiUrl); // Debugging: Gibt die API-URL aus
  
    this.http.get(apiUrl, {
      headers: {
        'Accept': 'text/csv',
        'Authorization': 'Basic NjA0ZGY5NWEtNjNmZi00YTU3LWJjYTUtNGYxMDlkZjEwN2Y1OnlYaG1PR1M0VjQwZ0FzV1VBYlJvU2h0SXMxRW41Q255',
        'Connection': 'keep-alive',
        'Accept-Encoding': 'gzip, deflate, br'
      },
      responseType: 'text' as 'json' // Erwartete Antwort als Text
    }).subscribe({
      next: (data) => {
        console.log('API-Antwort:', data); // Debugging: Gibt die erhaltenen Daten aus
        const parsedData = this.parseCsvData(data as string); // Typ-Cast auf string
        this.projects.push(...parsedData); // Hinzufügen der Daten zur Tabelle
        this.closeDialog(); // Schließen des Dialogs
      },
      error: (error) => {
        console.error('Fehler beim Abrufen des Projekts:', error);
        alert(`Fehler beim Abrufen des Projekts: ${error.message || 'Unbekannter Fehler'}`); // Fehlerbehandlung
      },
    });
  }
  

  private parseCsvData(csv: string): any[] {
    const lines = csv.split('\n'); // Aufteilen der CSV in Zeilen
    const headers = lines[0].split(','); // Erste Zeile als Header verwenden
    const rows = lines.slice(1); // Restliche Zeilen als Daten

    return rows
      .filter((row) => row.trim()) // Entfernt leere Zeilen
      .map((row) => {
        const values = row.split(','); // Aufteilen in Spalten
        const project: any = {};
        headers.forEach((header, index) => {
          project[header.trim()] = values[index]?.trim() || ''; // Zuordnung Header -> Werte
        });
        return project;
      });
  }
}
