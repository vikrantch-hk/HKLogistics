import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HkLogisticsSharedModule } from '../../shared';
import {
    CourierChannelService,
    CourierChannelPopupService,
    CourierChannelComponent,
    CourierChannelDetailComponent,
    CourierChannelDialogComponent,
    CourierChannelPopupComponent,
    CourierChannelDeletePopupComponent,
    CourierChannelDeleteDialogComponent,
    courierChannelRoute,
    courierChannelPopupRoute,
} from './';

const ENTITY_STATES = [
    ...courierChannelRoute,
    ...courierChannelPopupRoute,
];

@NgModule({
    imports: [
        HkLogisticsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CourierChannelComponent,
        CourierChannelDetailComponent,
        CourierChannelDialogComponent,
        CourierChannelDeleteDialogComponent,
        CourierChannelPopupComponent,
        CourierChannelDeletePopupComponent,
    ],
    entryComponents: [
        CourierChannelComponent,
        CourierChannelDialogComponent,
        CourierChannelPopupComponent,
        CourierChannelDeleteDialogComponent,
        CourierChannelDeletePopupComponent,
    ],
    providers: [
        CourierChannelService,
        CourierChannelPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HkLogisticsCourierChannelModule {}
