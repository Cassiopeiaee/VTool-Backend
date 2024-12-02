import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/project.service';

@Component({
  standalone: true,
  selector: 'app-project-list',
  template: `
    <div>
      <h1>Projektliste</h1>
      <table border="1" style="width: 100%; text-align: left;">
        <thead>
          <tr>
            <th>Projekt-ID</th>
            <th>Projektname</th>
            <th>Startdatum</th>
            <th>Enddatum</th>
            <th>Projektmanager</th>
            <th>Status</th>
            <th>Workflow</th>
          </tr>
        </thead>
        <tbody>
          <tr *ngFor="let project of projects">
            <td>{{ project.Id }}</td>
            <td>{{ project.Title }}</td>
            <td>{{ project.Start }}</td>
            <td>{{ project.End }}</td>
            <td>{{ project['Project Manager'] }}</td>
            <td>{{ project.Status }}</td>
            <td>{{ project.Workflow }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  `,
})
export class ProjectListComponent implements OnInit {
  projects: any[] = [];

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    const viewId = 'e813c779-f5ed-4fce-91ca-1ec9f67b0262';
    const filter = 'active';
    this.projectService.getProjects(viewId, filter).subscribe((data) => {
      this.projects = data;
    });
  }
}
