import { provideHttpClient, withFetch } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';

export const appConfig = {
  providers: [
    provideHttpClient(withFetch()), // Aktiviert die Fetch-API
    provideRouter(routes), // Fügt die Routing-Konfiguration hinzu
  ],
};
