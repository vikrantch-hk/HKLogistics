import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { CourierComponent } from './courier.component';
import { CourierDetailComponent } from './courier-detail.component';
import { CourierPopupComponent } from './courier-dialog.component';
import { CourierDeletePopupComponent } from './courier-delete-dialog.component';

@Injectable()
export class CourierResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const courierRoute: Routes = [
    {
        path: 'courier',
        component: CourierComponent,
        resolve: {
            'pagingParams': CourierResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Couriers'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'courier/:id',
        component: CourierDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Couriers'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const courierPopupRoute: Routes = [
    {
        path: 'courier-new',
        component: CourierPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Couriers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'courier/:id/edit',
        component: CourierPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Couriers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'courier/:id/delete',
        component: CourierDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Couriers'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
