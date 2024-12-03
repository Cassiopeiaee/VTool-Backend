import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { authInterceptorFn } from './services/auth-interceptor.service'; // Stelle sicher, dass der Pfad korrekt ist

export const appConfig = {
  providers: [
    provideHttpClient(withFetch(), withInterceptors([authInterceptorFn])), // Verwende die Interceptor-Funktion
    provideRouter(routes), // Fügt die Routing-Konfiguration hinzu
  ],
};
