import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Courier } from './courier.model';
import { CourierPopupService } from './courier-popup.service';
import { CourierService } from './courier.service';

@Component({
    selector: 'jhi-courier-delete-dialog',
    templateUrl: './courier-delete-dialog.component.html'
})
export class CourierDeleteDialogComponent {

    courier: Courier;

    constructor(
        private courierService: CourierService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.courierService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'courierListModification',
                content: 'Deleted an courier'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-courier-delete-popup',
    template: ''
})
export class CourierDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private courierPopupService: CourierPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.courierPopupService
                .open(CourierDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
