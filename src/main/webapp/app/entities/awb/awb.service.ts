import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { Awb } from './awb.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<Awb>;

@Injectable()
export class AwbService {

    private resourceUrl =  SERVER_API_URL + 'api/awbs';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/awbs';

    constructor(private http: HttpClient, private dateUtils: JhiDateUtils) { }

    create(awb: Awb): Observable<EntityResponseType> {
        const copy = this.convert(awb);
        return this.http.post<Awb>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(awb: Awb): Observable<EntityResponseType> {
        const copy = this.convert(awb);
        return this.http.put<Awb>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<Awb>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<Awb[]>> {
        const options = createRequestOption(req);
        return this.http.get<Awb[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Awb[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    search(req?: any): Observable<HttpResponse<Awb[]>> {
        const options = createRequestOption(req);
        return this.http.get<Awb[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<Awb[]>) => this.convertArrayResponse(res));
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: Awb = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<Awb[]>): HttpResponse<Awb[]> {
        const jsonResponse: Awb[] = res.body;
        const body: Awb[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to Awb.
     */
    private convertItemFromServer(awb: Awb): Awb {
        const copy: Awb = Object.assign({}, awb);
        copy.createDate = this.dateUtils
            .convertLocalDateFromServer(awb.createDate);
        return copy;
    }

    /**
     * Convert a Awb to a JSON which can be sent to the server.
     */
    private convert(awb: Awb): Awb {
        const copy: Awb = Object.assign({}, awb);
        copy.createDate = this.dateUtils
            .convertLocalDateToServer(awb.createDate);
        return copy;
    }
}
