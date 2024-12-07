import { Component } from '@angular/core';
import { ProjectListComponent } from './components/project-list/project-list.component';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-root',
  template: `
    <app-project-list></app-project-list> <!-- Beibehaltung der ProjectListComponent -->
    <router-outlet></router-outlet> <!-- Ermöglicht Navigation zu anderen Routen -->
  `,
  imports: [ProjectListComponent, RouterModule], // Import von ProjectListComponent und RouterModule
})
export class AppComponent {}
