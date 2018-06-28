import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { CourierChannel } from './courier-channel.model';
import { CourierChannelPopupService } from './courier-channel-popup.service';
import { CourierChannelService } from './courier-channel.service';

@Component({
    selector: 'jhi-courier-channel-delete-dialog',
    templateUrl: './courier-channel-delete-dialog.component.html'
})
export class CourierChannelDeleteDialogComponent {

    courierChannel: CourierChannel;

    constructor(
        private courierChannelService: CourierChannelService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.courierChannelService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'courierChannelListModification',
                content: 'Deleted an courierChannel'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-courier-channel-delete-popup',
    template: ''
})
export class CourierChannelDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courierChannelPopupService: CourierChannelPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.courierChannelPopupService
                .open(CourierChannelDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
