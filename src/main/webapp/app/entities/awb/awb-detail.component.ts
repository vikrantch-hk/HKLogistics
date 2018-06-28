import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Awb } from './awb.model';
import { AwbService } from './awb.service';

@Component({
    selector: 'jhi-awb-detail',
    templateUrl: './awb-detail.component.html'
})
export class AwbDetailComponent implements OnInit, OnDestroy {

    awb: Awb;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private awbService: AwbService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAwbs();
    }

    load(id) {
        this.awbService.find(id)
            .subscribe((awbResponse: HttpResponse<Awb>) => {
                this.awb = awbResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAwbs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'awbListModification',
            (response) => this.load(this.awb.id)
        );
    }
}
