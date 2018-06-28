import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { HkLogisticsSharedModule } from '../../shared';
import {
    AwbService,
    AwbPopupService,
    AwbComponent,
    AwbDetailComponent,
    AwbDialogComponent,
    AwbPopupComponent,
    AwbDeletePopupComponent,
    AwbDeleteDialogComponent,
    awbRoute,
    awbPopupRoute,
} from './';

const ENTITY_STATES = [
    ...awbRoute,
    ...awbPopupRoute,
];

@NgModule({
    imports: [
        HkLogisticsSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        AwbComponent,
        AwbDetailComponent,
        AwbDialogComponent,
        AwbDeleteDialogComponent,
        AwbPopupComponent,
        AwbDeletePopupComponent,
    ],
    entryComponents: [
        AwbComponent,
        AwbDialogComponent,
        AwbPopupComponent,
        AwbDeleteDialogComponent,
        AwbDeletePopupComponent,
    ],
    providers: [
        AwbService,
        AwbPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class HkLogisticsAwbModule {}
