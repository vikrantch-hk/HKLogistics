import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { CourierChannelComponent } from './courier-channel.component';
import { CourierChannelDetailComponent } from './courier-channel-detail.component';
import { CourierChannelPopupComponent } from './courier-channel-dialog.component';
import { CourierChannelDeletePopupComponent } from './courier-channel-delete-dialog.component';

export const courierChannelRoute: Routes = [
    {
        path: 'courier-channel',
        component: CourierChannelComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CourierChannels'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'courier-channel/:id',
        component: CourierChannelDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CourierChannels'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const courierChannelPopupRoute: Routes = [
    {
        path: 'courier-channel-new',
        component: CourierChannelPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CourierChannels'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'courier-channel/:id/edit',
        component: CourierChannelPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CourierChannels'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'courier-channel/:id/delete',
        component: CourierChannelDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'CourierChannels'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
