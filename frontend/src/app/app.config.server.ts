import { provideServerRendering } from '@angular/platform-server';
import { provideServerRoutesConfig } from '@angular/ssr';
import { appConfig } from './app.config';
import { serverRoutes } from './app.routes.server';

export const config = {
  providers: [
    ...appConfig.providers, // Übernimmt Provider aus appConfig
    provideServerRendering(),
    provideServerRoutesConfig(serverRoutes)
  ]
};
