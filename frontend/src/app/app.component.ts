import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-root',
  templateUrl: './app.component.html', // Externe Template-Datei verwenden
  imports: [RouterModule],
})
export class AppComponent {}
