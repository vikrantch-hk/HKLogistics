import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Awb } from './awb.model';
import { AwbPopupService } from './awb-popup.service';
import { AwbService } from './awb.service';

@Component({
    selector: 'jhi-awb-delete-dialog',
    templateUrl: './awb-delete-dialog.component.html'
})
export class AwbDeleteDialogComponent {

    awb: Awb;

    constructor(
        private awbService: AwbService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.awbService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'awbListModification',
                content: 'Deleted an awb'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-awb-delete-popup',
    template: ''
})
export class AwbDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private awbPopupService: AwbPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.awbPopupService
                .open(AwbDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
