import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IAwb } from 'app/shared/model/awb.model';
import { AwbService } from './awb.service';
import { ICourier } from 'app/shared/model/courier.model';
import { CourierService } from 'app/entities/courier';
import { IVendorWHCourierMapping } from 'app/shared/model/vendor-wh-courier-mapping.model';
import { VendorWHCourierMappingService } from 'app/entities/vendor-wh-courier-mapping';
import { IAwbStatus } from 'app/shared/model/awb-status.model';
import { AwbStatusService } from 'app/entities/awb-status';

@Component({
    selector: 'jhi-awb-update',
    templateUrl: './awb-update.component.html'
})
export class AwbUpdateComponent implements OnInit {
    private _awb: IAwb;
    isSaving: boolean;

    couriers: ICourier[];

    vendorwhcouriermappings: IVendorWHCourierMapping[];

    awbstatuses: IAwbStatus[];
    createDateDp: any;

    constructor(
        private jhiAlertService: JhiAlertService,
        private awbService: AwbService,
        private courierService: CourierService,
        private vendorWHCourierMappingService: VendorWHCourierMappingService,
        private awbStatusService: AwbStatusService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ awb }) => {
            this.awb = awb;
        });
        this.courierService.query().subscribe(
            (res: HttpResponse<ICourier[]>) => {
                this.couriers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.vendorWHCourierMappingService.query().subscribe(
            (res: HttpResponse<IVendorWHCourierMapping[]>) => {
                this.vendorwhcouriermappings = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.awbStatusService.query().subscribe(
            (res: HttpResponse<IAwbStatus[]>) => {
                this.awbstatuses = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.awb.id !== undefined) {
            this.subscribeToSaveResponse(this.awbService.update(this.awb));
        } else {
            this.subscribeToSaveResponse(this.awbService.create(this.awb));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IAwb>>) {
        result.subscribe((res: HttpResponse<IAwb>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCourierById(index: number, item: ICourier) {
        return item.id;
    }

    trackVendorWHCourierMappingById(index: number, item: IVendorWHCourierMapping) {
        return item.id;
    }

    trackAwbStatusById(index: number, item: IAwbStatus) {
        return item.id;
    }
    get awb() {
        return this._awb;
    }

    set awb(awb: IAwb) {
        this._awb = awb;
    }
}
