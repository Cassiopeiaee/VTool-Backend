import { Component } from '@angular/core';
import { CommonModule } from '@angular/common'; 
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-project-list',
  templateUrl: './project-list.component.html',
  styleUrls: ['./project-list.component.css'],
  standalone: true, // standalone-Komponente
  imports: [CommonModule], // Füge CommonModule hier hinzu
})
export class ProjectListComponent {
  csvData: string = '';
  tableData: string[][] = [];

  constructor(private projectService: ProjectService) {}

  ngOnInit(): void {
    this.projectService.getProjects('e813c779-f5ed-4fce-91ca-1ec9f67b0262', 'active', 'CSV').subscribe((data) => {
      this.csvData = data;
      this.parseCsvData();
    });
  }

  parseCsvData(): void {
    const lines = this.csvData.split('\n');
    this.tableData = lines.map((line) => line.split(','));
  }
}
