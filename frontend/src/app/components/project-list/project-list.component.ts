import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

// Schnittstelle für Projektdaten
interface Project {
  Title: string;
  Id: string;
  'Overall Status': string;
  Progress: number;
  Budget: string;
  'Start date': string;
  'End date': string;
  // Weitere Felder können hier hinzugefügt werden
}

@Component({
  standalone: true,
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
  imports: [FormsModule, CommonModule],
})
export class ProjectListComponent {
  // Für den Projekt Import
  viewId: string = '';
  projects: Project[] = [];

  // Felder für den Template-Endpunkt
  templateId: string = '';
  isPrivate: boolean = false;
  startDate: string = '';
  statusId: number = 0;
  title: string = '';
  projectType: string = '';
  folder: string = '';
  code: string = '';

  // Optionale Felder (Boolean) mit Standardwerten laut Doku
  adjustTaskDates: boolean = true;
  tasks: boolean = true;
  flavors: boolean = true;
  labels: boolean = true;
  manager: boolean = true;
  tasksWorkflow: boolean = true;
  tasksTemplate: boolean = true;
  folderTasksTemplate: boolean = true;
  viewSettings: boolean = true;
  budget: boolean = true;
  location: boolean = true;
  description: boolean = true;
  hourlyRate: boolean = true;
  goal: boolean = true;
  benefit: boolean = true;
  observers: boolean = true;
  permissions: boolean = true;
  blockTimeRecording: boolean = false;
  markedColumns: boolean = true;
  columnDescriptions: boolean = true;

  dialogOpen: boolean = false; // Für den Projekt Import Dialog

  constructor(private http: HttpClient, private router: Router) {}

  // Dialog für CSV-Projekt
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
        this.viewId = '';
      },
      error: (error) => {
        console.error('Fehler beim Abrufen des Projekts:', error);
        alert('Fehler beim Abrufen des Projekts. Bitte prüfen Sie die View-ID.');
      },
    });
  }

  saveProjectToDatabase(): void {
    if (this.projects.length === 0) {
      alert('Es gibt kein Projekt, das gespeichert werden kann.');
      return;
    }

    const apiUrl = 'http://localhost:8080/smenso/save-project';
    this.http.post(apiUrl, this.projects).subscribe({
      next: () => {
        alert('Projekte wurden erfolgreich in der Datenbank gespeichert.');
      },
      error: (error) => {
        console.error('Fehler beim Speichern der Projekte:', error);
        alert('Fehler beim Speichern der Projekte.');
      },
    });
  }

  private parseCsvData(csv: string): Project[] {
    console.log('Empfangene CSV-Daten:', csv);

    if (!csv.includes(',')) {
      console.error('Ungültige CSV-Daten: Kein gültiges CSV-Format', csv);
      return [];
    }

    const lines = csv.split('\n').filter(line => line.trim() !== '');
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

    const parsedData: Project[] = dataRows.map(row => {
      const values = row.split(',');
      const rowObject: any = {};
      headers.forEach((header, index) => {
        const key = header.trim();
        let value = values[index]?.trim() || 'Nicht verfügbar';

        // Budget bleibt als String
        if (key === 'Budget') {
          // Keine Konvertierung
        }

        rowObject[key] = value;
      });
      return rowObject as Project;
    });

    console.log('Parsed Data:', parsedData);
    return parsedData;
  }

  viewSavedProjects(): void {
    console.log("/gespeicherte Projekte anschauen");
    this.router.navigate(['/gespeicherte-projekte']);
  }

  // Methode zum Erstellen eines Projekts aus Vorlage
  createProjectFromTemplate(): void {
    if (!this.templateId || !this.title || !this.projectType || !this.folder) {
      alert('Bitte TemplateId, Title, ProjectType und Folder ausfüllen (Pflichtfelder).');
      return;
    }

    const xmlPayload = `
<create>
 <ProjectBase>
  <IsPrivate>${this.isPrivate}</IsPrivate>
  <StartDate>${this.startDate || '0001-01-01T01:00:00+01:00'}</StartDate>
  <StatusId>${this.statusId}</StatusId>
  <Title><![CDATA[${this.title}]]></Title>
  <ProjectType>${this.projectType}</ProjectType>
  <Folder>${this.folder}</Folder>
  <Code>${this.code}</Code>
 </ProjectBase>
 <AdjustTaskDates>${this.adjustTaskDates}</AdjustTaskDates>
 <Tasks>${this.tasks}</Tasks>
 <Flavors>${this.flavors}</Flavors>
 <Labels>${this.labels}</Labels>
 <Manager>${this.manager}</Manager>
 <TasksWorkflow>${this.tasksWorkflow}</TasksWorkflow>
 <TasksTemplate>${this.tasksTemplate}</TasksTemplate>
 <FolderTasksTemplate>${this.folderTasksTemplate}</FolderTasksTemplate>
 <ViewSettings>${this.viewSettings}</ViewSettings>
 <Budget>${this.budget}</Budget>
 <Location>${this.location}</Location>
 <Description>${this.description}</Description>
 <HourlyRate>${this.hourlyRate}</HourlyRate>
 <Goal>${this.goal}</Goal>
 <Benefit>${this.benefit}</Benefit>
 <Observers>${this.observers}</Observers>
 <Permissions>${this.permissions}</Permissions>
 <BlockTimeRecording>${this.blockTimeRecording}</BlockTimeRecording>
 <MarkedColumns>${this.markedColumns}</MarkedColumns>
 <ColumnDescriptions>${this.columnDescriptions}</ColumnDescriptions>
</create>
`.trim();

    const url = `http://localhost:8080/smenso/create-project-from-template/${this.templateId}`;

    const headers = new HttpHeaders({
      'Authorization': 'Basic NjA0ZGY5NWEtNjNmZi00YTU3LWJjYTUtNGYxMDlkZjEwN2Y1OnlYaG1PR1M0VjQwZ0FzV1VBYlJvU2h0SXMxRW41Q255', 
      'Content-Type': 'application/xml',
      'Accept': 'application/xml'
    });

    this.http.post(url, xmlPayload, { headers: headers, responseType: 'text' }).subscribe({
      next: (response) => {
        alert('Projekt wurde erfolgreich aus Vorlage erstellt: ' + response);
      },
      error: (error) => {
        console.error('Fehler beim Erstellen des Projekts aus Vorlage:', error);
        if (error.status === 0) {
          alert('Netzwerkfehler: Bitte überprüfen Sie Ihre Internetverbindung.');
        } else if (error.status === 401) {
          alert('Authentifizierungsfehler: Bitte überprüfen Sie Ihren Token.');
        } else if (error.status === 404) {
          alert('Endpunkt nicht gefunden: Bitte überprüfen Sie die URL und die templateId.');
        } else {
          alert(`Fehler beim Erstellen des Projekts: ${error.message}`);
        }
      }
    });
  }
}
