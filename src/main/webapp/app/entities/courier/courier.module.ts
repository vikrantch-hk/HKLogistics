import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HkLogisticsSharedModule } from '../../shared';
import {
    CourierService,
    CourierPopupService,
    CourierComponent,
    CourierDetailComponent,
    CourierDialogComponent,
    CourierPopupComponent,
    CourierDeletePopupComponent,
    CourierDeleteDialogComponent,
    courierRoute,
    courierPopupRoute,
    CourierResolvePagingParams,
} from './';

const ENTITY_STATES = [
    ...courierRoute,
    ...courierPopupRoute,
];

@NgModule({
    imports: [
        HkLogisticsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        CourierComponent,
        CourierDetailComponent,
        CourierDialogComponent,
        CourierDeleteDialogComponent,
        CourierPopupComponent,
        CourierDeletePopupComponent,
    ],
    entryComponents: [
        CourierComponent,
        CourierDialogComponent,
        CourierPopupComponent,
        CourierDeleteDialogComponent,
        CourierDeletePopupComponent,
    ],
    providers: [
        CourierService,
        CourierPopupService,
        CourierResolvePagingParams,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HkLogisticsCourierModule {}
