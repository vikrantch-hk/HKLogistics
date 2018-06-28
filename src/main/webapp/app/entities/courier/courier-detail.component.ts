import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Courier } from './courier.model';
import { CourierService } from './courier.service';

@Component({
    selector: 'jhi-courier-detail',
    templateUrl: './courier-detail.component.html'
})
export class CourierDetailComponent implements OnInit, OnDestroy {

    courier: Courier;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private courierService: CourierService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCouriers();
    }

    load(id) {
        this.courierService.find(id)
            .subscribe((courierResponse: HttpResponse<Courier>) => {
                this.courier = courierResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCouriers() {
        this.eventSubscriber = this.eventManager.subscribe(
            'courierListModification',
            (response) => this.load(this.courier.id)
        );
    }
}
