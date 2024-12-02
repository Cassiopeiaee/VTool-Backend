import { Component, OnInit } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
})
export class ProjectListComponent implements OnInit {
  projects = [
    {
      id: '1',
      name: 'Projekt Alpha',
      startDate: '2024-01-01',
      endDate: '2024-06-01',
      projectManager: 'Alice Johnson',
      status: 'In Bearbeitung',
      workflow: 'Genehmigung',
    },
    {
      id: '2',
      name: 'Projekt Beta',
      startDate: '2024-02-01',
      endDate: '2024-08-01',
      projectManager: 'Bob Smith',
      status: 'Abgeschlossen',
      workflow: 'Überprüfung',
    },
    {
      id: '3',
      name: 'Projekt Gamma',
      startDate: '2024-03-01',
      endDate: '2024-09-01',
      projectManager: 'Charlie Brown',
      status: 'Geplant',
      workflow: 'Genehmigung',
    },
  ]; // Beispiel-Daten

  ngOnInit(): void {
    // Hier kannst du später Daten aus einer API laden
  }
}
