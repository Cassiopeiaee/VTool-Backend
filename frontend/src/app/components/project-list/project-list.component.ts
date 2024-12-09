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
            console.log('CSV-Antwort erhalten:', response);

            if (response.trim().startsWith('Projekt erfolgreich gespeichert.')) {
                alert('Projekt wurde erfolgreich gespeichert, aber keine neuen CSV-Daten empfangen.');
                return;
            }

            const parsedData = this.parseCsvData(response);
            if (parsedData.length > 0) {
                this.projects = [...this.projects, ...parsedData];
                console.log('Aktualisierte Projekte:', this.projects);
            } else {
                alert('Die empfangenen Daten sind ungültig.');
            }

            this.closeDialog();
            this.viewId = ''; // Eingabefeld zurücksetzen
        },
        error: (error) => {
            console.error('Fehler beim Abrufen des Projekts:', error);
            alert('Fehler beim Abrufen des Projekts. Bitte prüfen Sie die View-ID.');
        },
    });
}

  
  

  // Speichert das erste Projekt in der Datenbank
  saveProjectToDatabase(): void {
    if (this.projects.length === 0) {
      alert('Es gibt kein Projekt, das gespeichert werden kann.');
      return;
    }

    const projectToSave = this.projects[0]; // Wähle das erste Projekt aus der Liste

    const apiUrl = 'http://localhost:8080/smenso/save-project'; 
    this.http.post(apiUrl, projectToSave).subscribe({
      next: () => {
        alert('Projekt wurde erfolgreich in der Datenbank gespeichert.');
      },
      error: (error) => {
        console.error('Fehler beim Speichern des Projekts:', error);
        alert('Fehler beim Speichern des Projekts.');
      },
    });
  }

  // Parsed CSV-Daten in ein Array von Objekten
  private parseCsvData(csv: string): any[] {
    console.log('Empfangene CSV-Daten:', csv);
  
    // Prüfen, ob die Daten im CSV-Format sind
    if (!csv.includes(',')) {
      console.error('Ungültige CSV-Daten: Kein gültiges CSV-Format', csv);
      return []; // Leeres Array zurückgeben
    }
  
    const lines = csv.split('\n').filter(line => line.trim() !== ''); // Leere Zeilen entfernen
    if (lines.length < 2) {
      console.error('Ungültige CSV-Daten: Zu wenig Zeilen', csv);
      return [];
    }
  
    const headers = lines[0]?.split(',');
    const dataRows = lines.slice(1);
  
    if (!headers || headers.length === 0) {
      console.error('Ungültige CSV-Daten: Header fehlen', csv);
      return [];
    }
  
    // Konvertiere jede Zeile in ein Objekt basierend auf den Headern
    const parsedData: any[] = dataRows.map(row => {
      const values = row.split(',');
      const rowObject: any = {};
      headers.forEach((header, index) => {
        rowObject[header.trim()] = values[index]?.trim() || 'Nicht verfügbar';
      });
      return rowObject;
    });
  
    console.log('Parsed Data:', parsedData); // Debugging-Ausgabe
  
    return parsedData; // Array von Objekten zurückgeben
  }
  
  
  

  // Navigiert zur Seite "Gespeicherte Projekte"
  viewSavedProjects(): void {
    console.log("/gespeichterte Projekt anschauen");
    this.router.navigate(['/gespeicherte-projekte']); 
  }
}
