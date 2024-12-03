import { Component } from '@angular/core';
import { ProjectListComponent } from './components/project-list/project-list.component';

@Component({
  standalone: true,
  selector: 'app-root',
  template: `<app-project-list></app-project-list>`, // Einbinden der ProjectListComponent
  imports: [ProjectListComponent], // Import der Standalone-Komponente
})
export class AppComponent {}
