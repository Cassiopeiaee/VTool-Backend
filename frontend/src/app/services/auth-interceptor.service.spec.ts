import { HttpRequest } from '@angular/common/http';
import { authInterceptorFn } from './auth-interceptor.service';
import { of } from 'rxjs';

describe('authInterceptorFn', () => {
  it('should add Authorization header to request', () => {
    const apiKey = 'N2E4NzU5YjItY2NlMC00MTQzLWIzMmYtM2Q4ZTljNzdkY2UxOk1ab0loNDJLQ01yR1VLVmNBSGN3ZHNHWXJkUnU1cGhl';
    const authHeader = `Basic ${apiKey}`;

    // Mock Request and Handler
    const mockRequest = new HttpRequest('GET', '/test');
    const mockHandler = {
      handle: (req: HttpRequest<any>) => {
        expect(req.headers.get('Authorization')).toEqual(authHeader);
        return of({});
      },
    };

    // Execute the interceptor function
    authInterceptorFn(mockRequest, mockHandler as any);
  });
});
