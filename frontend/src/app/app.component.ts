// src/app/app.component.ts

import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true, // Deklaration als Standalone-Komponente
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [RouterModule], // Importieren von RouterModule für Routing-Funktionen
})
export class AppComponent {
  title = 'frontend';
}
