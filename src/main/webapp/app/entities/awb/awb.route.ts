import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { AwbComponent } from './awb.component';
import { AwbDetailComponent } from './awb-detail.component';
import { AwbPopupComponent } from './awb-dialog.component';
import { AwbDeletePopupComponent } from './awb-delete-dialog.component';

export const awbRoute: Routes = [
    {
        path: 'awb',
        component: AwbComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Awbs'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'awb/:id',
        component: AwbDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Awbs'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const awbPopupRoute: Routes = [
    {
        path: 'awb-new',
        component: AwbPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Awbs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'awb/:id/edit',
        component: AwbPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Awbs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'awb/:id/delete',
        component: AwbDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Awbs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
