import { Component } from '@angular/core';
import { ProjectListComponent } from './components/project-list/project-list.component';

@Component({
  standalone: true,
  selector: 'app-root',
  template: `
    <h1>{{ title }}</h1>
    <app-project-list></app-project-list>
  `,
  styleUrls: ['./app.component.css'],
  imports: [ProjectListComponent], // Importiere die Standalone-Komponente
})
export class AppComponent {
  title = 'Projektliste'; // Titel-Eigenschaft hinzugefügt
}
