import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Courier } from './courier.model';
import { CourierPopupService } from './courier-popup.service';
import { CourierService } from './courier.service';
import { CourierGroup, CourierGroupService } from '../courier-group';

@Component({
    selector: 'jhi-courier-dialog',
    templateUrl: './courier-dialog.component.html'
})
export class CourierDialogComponent implements OnInit {

    courier: Courier;
    isSaving: boolean;

    couriergroups: CourierGroup[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private courierService: CourierService,
        private courierGroupService: CourierGroupService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.courierGroupService.query()
            .subscribe((res: HttpResponse<CourierGroup[]>) => { this.couriergroups = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.courier.id !== undefined) {
            this.subscribeToSaveResponse(
                this.courierService.update(this.courier));
        } else {
            this.subscribeToSaveResponse(
                this.courierService.create(this.courier));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Courier>>) {
        result.subscribe((res: HttpResponse<Courier>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Courier) {
        this.eventManager.broadcast({ name: 'courierListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCourierGroupById(index: number, item: CourierGroup) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-courier-popup',
    template: ''
})
export class CourierPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courierPopupService: CourierPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.courierPopupService
                    .open(CourierDialogComponent as Component, params['id']);
            } else {
                this.courierPopupService
                    .open(CourierDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
