import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { Courier } from './courier.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Courier>;

@Injectable()
export class CourierService {

    private resourceUrl =  SERVER_API_URL + 'api/couriers';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/couriers';

    constructor(private http: HttpClient) { }

    create(courier: Courier): Observable<EntityResponseType> {
        const copy = this.convert(courier);
        return this.http.post<Courier>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(courier: Courier): Observable<EntityResponseType> {
        const copy = this.convert(courier);
        return this.http.put<Courier>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Courier>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Courier[]>> {
        const options = createRequestOption(req);
        return this.http.get<Courier[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Courier[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Courier[]>> {
        const options = createRequestOption(req);
        return this.http.get<Courier[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Courier[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Courier = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Courier[]>): HttpResponse<Courier[]> {
        const jsonResponse: Courier[] = res.body;
        const body: Courier[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Courier.
     */
    private convertItemFromServer(courier: Courier): Courier {
        const copy: Courier = Object.assign({}, courier);
        return copy;
    }

    /**
     * Convert a Courier to a JSON which can be sent to the server.
     */
    private convert(courier: Courier): Courier {
        const copy: Courier = Object.assign({}, courier);
        return copy;
    }
}
