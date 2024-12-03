import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Für ngModel
import { CommonModule } from '@angular/common'; // Für grundlegende Angular-Direktiven
import { HttpClient } from '@angular/common/http'; // Für API-Anfragen

@Component({
  standalone: true,
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
  imports: [FormsModule, CommonModule], // FormsModule und CommonModule importieren
})
export class ProjectListComponent {
  projects: any[] = []; // Array mit Projektdaten
  dialogOpen = false;
  viewId = ''; // Variable für die View-ID

  constructor(private http: HttpClient) {}

  openDialog(): void {
    this.dialogOpen = true;
  }

  closeDialog(): void {
    this.dialogOpen = false;
  }
  

  fetchProject(): void {
    if (!this.viewId.trim()) {
      alert('Bitte geben Sie eine gültige View-ID ein.');
      return;
    }
  
    const backendUrl = `http://localhost:8080/api/projects/report/${this.viewId}`;
    this.http.get(backendUrl, { responseType: 'text' }).subscribe({
      next: (response) => {
        console.log('CSV Data:', response);
        const parsedData = this.parseCsvData(response);
        this.projects.push(...parsedData);
      },
      error: (error) => {
        console.error('Fehler beim Abrufen des Projekts:', error);
        alert('Fehler beim Abrufen des Projekts. Bitte prüfen Sie die View-ID.');
      },
    });
  }
  

  parseCsvData(csv: string): any[] {
    const lines = csv.split('\n');
    const headers = lines[0].split(',');
    return lines.slice(1).map((line) => {
      const values = line.split(',');
      const project: any = {};
      headers.forEach((header, index) => {
        project[header.trim()] = values[index]?.trim() || '';
      });
      return project;
    });
  }

  getKeys(obj: any): string[] {
    return Object.keys(obj); 
  }
}
