import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptorFn: HttpInterceptorFn = (req, next) => {
  const authHeader = `Basic 2E4NzU5YjItY2NlMC00MTQzLWIzMmYtM2Q4ZTljNzdkY2UxOk1ab0loNDJLQ01yR1VLVmNBSGN3ZHNHWXJkUnU1cGhl`;

  // Klonen der Anfrage und Hinzufügen des Headers
  const clonedRequest = req.clone({
    setHeaders: {
      Authorization: authHeader,
    },
  });

  return next(clonedRequest);
};
