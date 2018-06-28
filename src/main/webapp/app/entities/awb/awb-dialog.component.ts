import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Awb } from './awb.model';
import { AwbPopupService } from './awb-popup.service';
import { AwbService } from './awb.service';
import { Channel, ChannelService } from '../channel';
import { VendorWHCourierMapping, VendorWHCourierMappingService } from '../vendor-wh-courier-mapping';
import { AwbStatus, AwbStatusService } from '../awb-status';

@Component({
    selector: 'jhi-awb-dialog',
    templateUrl: './awb-dialog.component.html'
})
export class AwbDialogComponent implements OnInit {

    awb: Awb;
    isSaving: boolean;

    channels: Channel[];

    vendorwhcouriermappings: VendorWHCourierMapping[];

    awbstatuses: AwbStatus[];
    createDateDp: any;

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private awbService: AwbService,
        private channelService: ChannelService,
        private vendorWHCourierMappingService: VendorWHCourierMappingService,
        private awbStatusService: AwbStatusService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.channelService.query()
            .subscribe((res: HttpResponse<Channel[]>) => { this.channels = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.vendorWHCourierMappingService.query()
            .subscribe((res: HttpResponse<VendorWHCourierMapping[]>) => { this.vendorwhcouriermappings = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.awbStatusService.query()
            .subscribe((res: HttpResponse<AwbStatus[]>) => { this.awbstatuses = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.awb.id !== undefined) {
            this.subscribeToSaveResponse(
                this.awbService.update(this.awb));
        } else {
            this.subscribeToSaveResponse(
                this.awbService.create(this.awb));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Awb>>) {
        result.subscribe((res: HttpResponse<Awb>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Awb) {
        this.eventManager.broadcast({ name: 'awbListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackChannelById(index: number, item: Channel) {
        return item.id;
    }

    trackVendorWHCourierMappingById(index: number, item: VendorWHCourierMapping) {
        return item.id;
    }

    trackAwbStatusById(index: number, item: AwbStatus) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-awb-popup',
    template: ''
})
export class AwbPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private awbPopupService: AwbPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.awbPopupService
                    .open(AwbDialogComponent as Component, params['id']);
            } else {
                this.awbPopupService
                    .open(AwbDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
