import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { CourierChannel } from './courier-channel.model';
import { CourierChannelPopupService } from './courier-channel-popup.service';
import { CourierChannelService } from './courier-channel.service';
import { Channel, ChannelService } from '../channel';
import { Courier, CourierService } from '../courier';

@Component({
    selector: 'jhi-courier-channel-dialog',
    templateUrl: './courier-channel-dialog.component.html'
})
export class CourierChannelDialogComponent implements OnInit {

    courierChannel: CourierChannel;
    isSaving: boolean;

    channels: Channel[];

    couriers: Courier[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private courierChannelService: CourierChannelService,
        private channelService: ChannelService,
        private courierService: CourierService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.channelService.query()
            .subscribe((res: HttpResponse<Channel[]>) => { this.channels = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.courierService.query()
            .subscribe((res: HttpResponse<Courier[]>) => { this.couriers = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.courierChannel.id !== undefined) {
            this.subscribeToSaveResponse(
                this.courierChannelService.update(this.courierChannel));
        } else {
            this.subscribeToSaveResponse(
                this.courierChannelService.create(this.courierChannel));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<CourierChannel>>) {
        result.subscribe((res: HttpResponse<CourierChannel>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: CourierChannel) {
        this.eventManager.broadcast({ name: 'courierChannelListModification', content: 'OK'});
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

    trackCourierById(index: number, item: Courier) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-courier-channel-popup',
    template: ''
})
export class CourierChannelPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courierChannelPopupService: CourierChannelPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.courierChannelPopupService
                    .open(CourierChannelDialogComponent as Component, params['id']);
            } else {
                this.courierChannelPopupService
                    .open(CourierChannelDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
