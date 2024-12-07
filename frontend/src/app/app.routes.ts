import { Routes } from '@angular/router';
import { ProjectListComponent } from './components/project-list/project-list.component';
import { SavedProjectsComponent } from './components/saved-projects/saved-projects.component'; 

export const routes: Routes = [
  { path: '', component: ProjectListComponent }, // Startseite
  { path: 'gespeicherte-projekte', component: SavedProjectsComponent }, // Neue Seite für gespeicherte Projekte
];
