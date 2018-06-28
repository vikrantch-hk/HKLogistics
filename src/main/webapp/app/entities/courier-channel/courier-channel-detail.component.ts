import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { CourierChannel } from './courier-channel.model';
import { CourierChannelService } from './courier-channel.service';

@Component({
    selector: 'jhi-courier-channel-detail',
    templateUrl: './courier-channel-detail.component.html'
})
export class CourierChannelDetailComponent implements OnInit, OnDestroy {

    courierChannel: CourierChannel;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private courierChannelService: CourierChannelService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCourierChannels();
    }

    load(id) {
        this.courierChannelService.find(id)
            .subscribe((courierChannelResponse: HttpResponse<CourierChannel>) => {
                this.courierChannel = courierChannelResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCourierChannels() {
        this.eventSubscriber = this.eventManager.subscribe(
            'courierChannelListModification',
            (response) => this.load(this.courierChannel.id)
        );
    }
}
