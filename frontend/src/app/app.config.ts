import { ApplicationConfig, provideHttpClient, withFetch } from '@angular/common/http';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withFetch()), // Aktiviert die Fetch-API
  ],
};
