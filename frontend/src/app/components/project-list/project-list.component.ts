import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
})
export class ProjectListComponent implements OnInit {
  csvData: string = '';

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    this.projectService.getProjects('e813c779-f5ed-4fce-91ca-1ec9f67b0262', 'active', 'CSV').subscribe((data) => {
      this.csvData = data;
      this.parseCsvData();
    });
  }

  parseCsvData(): void {
    // Füge deine Logik zur CSV-Verarbeitung hinzu
    console.log(this.csvData);
  }
}
